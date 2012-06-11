define([ "dojo/_base/declare", "dojo/_base/xhr", "dojo/_base/array", "./BatteryWidget", "dojo/dom-construct",
         "./_SelectableGroup", "dijit/registry", "dojo/_base/connect"], 
		function(declare, xhr, array, BatteryWidget, domConstruct, SelectableGroup, registry, connect) {

	return declare(null, {
		// PROPERTIES
		
		/*public TitlePane*/ batteriesPane: null,
		
		/*private map*/batteryTypes: null,
		
		selectableGroup: null,

		constructor : function(args) {
			declare.safeMixin(this, args);
			var baseUrl = dojo.config.batts.baseurl;
			var outer = this;
			
			this.selectableGroup = new SelectableGroup();
			var btnBoughtMore = registry.byId("btnBoughtMore");
			connect.connect(this.selectableGroup, "onSelected", btnBoughtMore, function() {
				btnBoughtMore.set('disabled', false);
			});
			connect.connect(this.selectableGroup, "onDeselected", btnBoughtMore, function() {
				btnBoughtMore.set('disabled', true);
			});
			
			function addBatteryWidgets(result) {
				array.forEach(result, function(entry) {
					console.log("Type="+entry.batteryTypeKey+", Available="+entry.count);
					var batteryWidget = new BatteryWidget({type:entry.batteryTypeKey});
					console.log("about to startup widget");
					batteryWidget.startup();
					domConstruct.place(batteryWidget.domNode, this.batteriesPane.containerNode);
					this.selectableGroup.addSelectable(batteryWidget);
				}, outer);
			}
			function availableBattsLoader() {
				xhr.get({
					url : baseUrl+"household/api/available",
					handleAs : "json",
					load : function(result) {
						addBatteryWidgets(result);
					}
				});
			}

			console.log("About to get types");
			xhr.get({
				url : baseUrl+"battery/api/types",
				handleAs : "json",
				load : function(result) {
					this.batteryTypes = result;
					availableBattsLoader();
				}
			});
		},

		// METHODS
		labelOfBatteryType: function(key) {
			if (batteryTypes) {
				return batteryTypes[key] ? batteryTypes[key].label : key;
			}
			else {
				return key;
			}
		},

	}); // end of declare
}); // end of require
