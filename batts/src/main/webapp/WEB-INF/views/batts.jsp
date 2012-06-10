<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>batts</title>

<spring:url value="/" var="baseurl" />
<link rel="stylesheet"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.7.2/dijit/themes/claro/claro.css"
	media="screen">
<!-- work around until Dojo 1.8 release -->
<link rel="stylesheet"
	href="http://ajax.googleapis.com/ajax/libs/dojo/1.7.2/dijit/themes/dijit.css"
	media="screen">
<link href="${baseurl}/static/css/main.css" rel="stylesheet"
	type="text/css">

<script>
dojoConfig = {
	async: true, 
	tlmSiblingOfDojo: false,
	batts: {
		baseurl: "${baseurl}"
	},
	packages:[
		{name:"batts", location:"${baseurl}/static/js/batts"}
	]
}
</script>
<script src="http://ajax.googleapis.com/ajax/libs/dojo/1.7.2/dojo/dojo.js"></script>

<script>
	require([ "dojo/parser", "dijit/registry", "batts", "dijit/layout/SplitContainer", "dijit/TitlePane",
			"dijit/layout/ContentPane", "dijit/form/Button", "dijit/layout/LayoutContainer", "dijit/layout/ContentPane", 
			"dojo/domReady!" ], 
		function(parser, registry, batts, SplitContainer){
			parser.parse();
			var mainModule = new batts({batteriesPane:registry.byId('batteries')});
	});
</script>

</head>
<body class="claro">

	<div id="appLayout" data-dojo-type="dijit.layout.SplitContainer">
		<div id="batteries" data-dojo-type="dijit.TitlePane"
			data-dojo-props="title: 'Batteries', toggleable:false" style="width: 50%"></div>
			
		<div id="actionsPane" data-dojo-type="dijit.layout.ContentPane" style="width: 110px; padding: 5px;">
			<div id="btnBoughtMore" layoutAlign="right" data-dojo-type="dijit.form.Button">Bought More</div>
			<div id="btnPutInDevice" layoutAlign="right" data-dojo-type="dijit.form.Button">Put in device</div>
			
		</div>

		<div id="devices" data-dojo-type="dijit.TitlePane"
			data-dojo-props="title: 'Devices', toggleable:false">
				<div id="devices-inuse" data-dojo-type="dijit.TitlePane"
					data-dojo-props="title: 'In-use'">in-use</div>
				<div id="devices-unused" data-dojo-type="dijit.TitlePane"
					data-dojo-props="title: 'Unused'">unused</div>
		</div>
	</div>

</body>
</html>