<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<title>batts - organizing your batteries</title>

<spring:url value="/" var="baseurl" />
<spring:url value="/static" var="resourceurl" />
<spring:url value="/static/js/jquery" var="jqueryJsBase" />

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="absURL" value="${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI, pageContext.request.contextPath)}" />


<meta property="og:description" content="A simple little web application that helps you keep track of all your rechargeable batteries."/>
<meta property="og:image" content="${absURL}/static/img/BattsPreview.png"/>

<script type="text/javascript">
	config = {
		baseurl : "${baseurl}",
		resourceurl : "${resourceurl}",
		
		getBaseUrl : function() {
			if (this.baseurl.charAt(this.baseurl.length - 1) == '/') {
				return this.baseurl.substr(0, this.baseurl.length - 1);
			}
			else {
				return this.baseurl;
			}
		},
		
		buildUrl : function(subpath) {
			return this.getBaseUrl() + subpath;
		},

		buildAjaxUrl : function(subpath) {
			return this.getBaseUrl() + subpath;
		}
	}
</script>
<link href="${resourceurl}/css/jquery/ui-lightness/jquery-ui-1.8.21.custom.css" rel="Stylesheet" type="text/css">
<link href="${resourceurl}/css/jquery/jquery.qtip.css" rel="Stylesheet" type="text/css">
<link href="${resourceurl}/css/batts.css" rel="Stylesheet" type="text/css">


<script src="${jqueryJsBase}/jquery-1.7.2.js"></script>
<script src="${jqueryJsBase}/jquery-ui-1.8.21.custom.min.js"></script>
<script src="${jqueryJsBase}/jquery.validate.js"></script>
<script src="${jqueryJsBase}/jquery.qtip.js"></script>
