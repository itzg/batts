<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ include file="/WEB-INF/pieces/common-head.jsp" %>

<script>

$(document).ready(function() {

	$("button").button();
	
	$("#joinForm").validate({
		rules:{
	           shareCode: {required:true, min:1000, max:9999},
	       }
	});
});

</script>

</head>
<body>
<div id="banner"></div>
<h1>...one more thing</h1>
<div id="introPara">
You appear to be new here. In one click we can get you setup with a new household area to
keep track of those elusive rechargeable batteries. 
</div>

<div>
<a href="create" id="getStarted">Get Started</a>
</div>

<h2>Joining a household?</h2>
<form id="joinForm" action="${baseurl}/join" method="post">
Enter the household share code. Any existing member of the household can generate this.
<div>
<input name="shareCode" type="number" size="4" maxlength="4"></input>
</div>
<button type="submit">Join</button>
</form>

<%@ include file="/WEB-INF/pieces/footer.jsp" %>
</body>
</html>