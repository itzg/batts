<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ include file="/WEB-INF/pieces/common-head.jsp"%>

<link href="${resourceurl}/openid-selector/css/openid.css" rel="Stylesheet" type="text/css">
<script type="text/javascript" src="${resourceurl}/openid-selector/js/openid-jquery.js"></script>
<script type="text/javascript" src="${resourceurl}/openid-selector/js/openid-en.js"></script>


	<script type="text/javascript">
		$(document).ready(function() {
			openid.img_path = "${resourceurl}/openid-selector/images/";
			openid.init('openid_identifier');
		});
	</script>

</head>
<body>
	<div id="banner"></div>
	<h1>Hi. Who are you?</h1>
	<div class="description">
		Every household gets their own private area to keep track of their batteries. 
		In order to know who you are and what batteries belong to you, we
		just need you authenticate with one of the providers below -- no further information
		is needed or retrieved.
	</div>
	<spring:url value="/j_spring_openid_security_check"
		var="form_url_openid" />

	<form action="${form_url_openid}" method="post" id="openid_form">
		<input type="hidden" name="action" value="verify" />
			<div id="openid_choice">
				<div id="openid_btns"></div>
			</div>
			<noscript>
				<p>OpenID is service that allows you to log-on to many different websites using a single indentity.
				Find out <a href="http://openid.net/what/">more about OpenID</a> and <a href="http://openid.net/get/">how to get an OpenID enabled account</a>.</p>
			</noscript>
	</form>

<%@ include file="/WEB-INF/pieces/footer.jsp" %>

</body>
</html>