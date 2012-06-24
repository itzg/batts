<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Batts</title>

<spring:url value="/" var="baseurl" />
<spring:url value="/static" var="resourceurl" />
<script type="text/javascript">
config = {
    baseurl: "${baseurl}",
    resourceurl: "${resourceurl}"
}
</script>
<link href="${resourceurl}/css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="Stylesheet" type="text/css">
<link href="${resourceurl}/css/batts.css" rel="Stylesheet" type="text/css">
<link href="../../resources/css/batts.css" rel="Stylesheet" type="text/css">

<script src="${resourceurl}/js/jquery-1.7.2.js"></script>
<script src="${resourceurl}/js/jquery-ui-1.8.21.custom.min.js"></script>
<script src="${resourceurl}/js/jquery.validate.js"></script>
<script src="${resourceurl}/js/batts.js"></script>

</head>
<body>
    <div id="batteries">
        <h1>Batteries</h1>
        <div class="content">
        </div>
    </div>
    <div id="actions">
        <button id="btnAddMore">Add More Batteries</button>
    </div>
    <div id="devices">
        <h1>Devices</h1>
        <div class="content"></div>
    </div>
    <div id="dialogs">
        <div id="dlgAddMore">
            <form method="post" action="#">
            <div>
            <label>How many</label>
            <input type="text" name="count" value="2"></input>
            </div>
            <button id="dlgAddMore-add" type="submit">Add</button>
            </form>
        </div>
    </div>
</body>
</html>