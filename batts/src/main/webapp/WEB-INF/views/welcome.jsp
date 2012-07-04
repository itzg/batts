<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to batts</title>

<spring:url value="/" var="baseurl" />
<spring:url value="/static" var="resourceurl" />
<script type="text/javascript">
    config = {
        baseurl : "${baseurl}",
        resourceurl : "${resourceurl}",
        
        buildAjaxUrl: function(subpath) {
           return this.baseurl == "/" ? subpath : this.baseurl + subpath;
        }
    }
</script>
<link href="${resourceurl}/css/ui-lightness/jquery-ui-1.8.21.custom.css" rel="Stylesheet"
    type="text/css">
<link href="${resourceurl}/css/batts.css" rel="Stylesheet" type="text/css">
<link href="../../resources/css/batts.css" rel="Stylesheet" type="text/css">

<script src="${resourceurl}/js/jquery-1.7.2.js"></script>
<script src="${resourceurl}/js/jquery-ui-1.8.21.custom.min.js"></script>

</head>
<body>
<h1>Welcome to batts</h1>
<div id="introPara">
You appear to be new here. In one click we can get you setup with a new household area to
keep track of those pesky rechargeable batteries. 
</div>
<a href="create" id="getStarted">Get Started</a>
</body>
</html>