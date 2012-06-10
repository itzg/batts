define([ "dojo/_base/declare", "dojo/_base/xhr", "dojo/_base/array", "./BatteryWidget", "dojo/dom-construct"], 
		function(declare, xhr, array, BatteryWidget, domConstruct) {

	return declare(null, {
		// PROPERTIES
		
		/*public TitlePane*/ batteriesPane: null,
		
		/*private map*/batteryTypes: null,

		constructor : function(args) {
			declare.safeMixin(this, args);
			var baseUrl = dojo.config.batts.baseurl;
			var outer = this;
			function addBatteryWidgets(result) {
				array.forEach(result, function(entry) {
					console.log("Type="+entry.batteryTypeKey+", Available="+entry.count);
					var batteryWidget = new BatteryWidget({type:entry.batteryTypeKey});
					console.log("about to startup widget");
					batteryWidget.startup();
					console.log("started widget, about to place");
					console.log(this.batteriesPane);
					console.log(batteryWidget);
					domConstruct.place(batteryWidget.domNode, this.batteriesPane.containerNode);
//					batteriesPane.addChild(batteryWidget);
					console.log("placed");
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
