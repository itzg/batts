<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ include file="/WEB-INF/pieces/common-head.jsp"%>

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

	<form action="${form_url_openid}" method="post">
		<input name="openid_identifier" size="50" maxlength="100"
			type="hidden" value="http://www.google.com/accounts/o8/id" />
		<div class="submit">
			<input id="proceed_google" type="submit" value="Sign in with Google" />
		</div>
	</form>

	<div style="font-style: italic;font-size: small; margin-top: 10px">...sorry, there's only one provider for now</div>
	
<jsp:directive.include file="/WEB-INF/pieces/footer.jsp"/>

</body>
</html>