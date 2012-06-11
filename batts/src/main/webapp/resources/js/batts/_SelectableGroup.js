define([ "dojo/_base/declare", "dojo/_base/connect", "dojo/_base/array" ], 
		function(declare, connect,array) {

	return declare(null, {
		/* PARMS */
		selectables: [],
		links: [],
		count: 0,

		/* METHODS */
		handleSelectionChange: function(source, newValue) {
			console.debug("Saw selection change of "+source+" to "+newValue);
			var oldCount = this.count;
			if (newValue) {
				this.count++;
			}
			else {
				this.count--;
			}
			if (oldCount == 0 && this.count > 0) {
				this.onSelected();
			}
			else if (oldCount > 0 && this.count == 0) {
				this.onDeselected();
			}
			if (newValue) {
				var pos = this.selectables.indexOf(source);
				if (pos >= 0) {
					array.forEach(this.selectables, function(entry,i) {
						if (i != pos && entry.isSelected()) {
							entry.setSelected(false);
						}
					});
				}
				this.onSelectionChanged();
			}
		},
		
		addSelectable: function(/*_SelectableMixin,_WidgetBase*/ selectable) {
			console.debug("adding selectable "+selectable);
			var link = connect.connect(selectable, "onSelectionChanged", this, "handleSelectionChange");
			this.selectables.push(selectable);
			this.links.push(link);
		},
		
		/* EVENTS */
		onSelectionChanged: function() {console.log("onSelectionChanged");},
		onSelected: function() {console.log("onSelected");},
		onDeselected: function() {console.log("onDeselected");}
	});
});