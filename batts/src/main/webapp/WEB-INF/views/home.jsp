<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>

<spring:url value="/" var="baseurl" />

<link href="${baseurl}/resources/main.css" rel="Stylesheet" type="text/css">

<script type="text/javascript">
dojoConfig= {
	parseOnLoad: false,
	async: true
};
</script>
<script
	src="http://ajax.googleapis.com/ajax/libs/dojo/1.7.1/dojo/dojo.js"></script>
<script type="text/javascript">

var batteryTypes;

function labelOfBatteryType(key) {
	if (batteryTypes) {
		return batteryTypes[key] ? batteryTypes[key].label : key;
	}
	else {
		return key;
	}
}

require(["dojo/_base/xhr","dojo/dom", "dojo/dom-construct","dojo/_base/array",
         "dojo/on","dojo/dom-class","dojo/mouse","dojo/domReady!"],
		function(xhr,dom,domConstruct,array,on,domClass,mouse) {
			function batteryTypeInfoHandler(batteryTypes) {
				var listDom = domConstruct.place("<ul/>", "batteryTypesInfo");
				
				for (var key in batteryTypes) {
					domConstruct.place("<li>"+batteryTypes[key].label+"</li>", listDom);
				}
			};
			
			function startBatteryEntryHover(evt) {
				domClass.add(this, "hover-battery-entry");
			}
			
			function stopBatteryEntryHover(evt) {
				domClass.remove(this, "hover-battery-entry");
			}
			
			function availableBattsLoader() {
				xhr.get({
					url: "${baseurl}/household/api/available",
					handleAs: "json",
					load: function(result) {
						var containerDom = dom.byId("availableBattsInfo");
						
						array.forEach(result, function(entry) {
							var tableRow = domConstruct.place("<div class='battery-entry'/>", containerDom);
							
							var summaryLine = domConstruct.place("<div class='summary-line'/>", tableRow);
							domConstruct.place("<span class='battery-type-col'>"+labelOfBatteryType(entry.batteryTypeKey)+"</span>", 
								summaryLine);
							domConstruct.place("<span class='battery-x-col'>x</span>", 
								summaryLine);
							domConstruct.place("<span class='battery-count-col'>"+entry.count+"</span>", 
								summaryLine);
							
							on(tableRow, mouse.enter, startBatteryEntryHover);
							on(tableRow, mouse.leave, stopBatteryEntryHover);
						});
					}
				});
			}
			
			xhr.get({
				url: "${baseurl}/battery/api/types",
				handleAs: "json",
				load: function(result) {
					batteryTypes = result;
					availableBattsLoader();
					batteryTypeInfoHandler(batteryTypes);
				}
			});
	});
</script>
</head>
<body>
	<div id="batteryTypesInfo">
		<h1>Known battery types</h1>
	</div>
	
	<div id="availableBattsInfo">
		<h1>Available Batteries</h1>
	</div>
		
</body>
</html>
