<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/" var="baseurl" />
<spring:url value="/static" var="resourceurl" />
<spring:url value="/static/js/jquery" var="jqueryJsBase" />
<script type="text/javascript">
    config = {
        baseurl : "${baseurl}",
        resourceurl : "${resourceurl}",
        
        buildAjaxUrl: function(subpath) {
           return this.baseurl == "/" ? subpath : this.baseurl + subpath;
        }
    }
</script>
<link href="${resourceurl}/css/jquery/ui-lightness/jquery-ui-1.8.21.custom.css" rel="Stylesheet" type="text/css">
<link href="${resourceurl}/css/jquery/jquery.qtip.css" rel="Stylesheet" type="text/css">
<link href="${resourceurl}/css/batts.css" rel="Stylesheet" type="text/css">
<link href="../../resources/css/batts.css" rel="Stylesheet" type="text/css">


<script src="${jqueryJsBase}/jquery-1.7.2.js"></script>
<script src="${jqueryJsBase}/jquery-ui-1.8.21.custom.min.js"></script>
<script src="${jqueryJsBase}/jquery.validate.js"></script>
<script src="${jqueryJsBase}/jquery.qtip.js"></script>
<script src="${resourceurl}/js/batts.js"></script>
