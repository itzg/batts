<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>batts login</title>

<%@ include file="/WEB-INF/pieces/common-head.jsp" %>

</head>
<body>
  <spring:url value="/j_spring_openid_security_check" var="form_url_openid" />

    <form action="${form_url_openid}" method="post">
        <input name="openid_identifier" size="50"
               maxlength="100" type="hidden"
               value="http://www.google.com/accounts/o8/id"/>
        <div class="submit">
            <input id="proceed_google" type="submit" value="Sign in with Google" />
        </div>
    </form>

</body>
</html>