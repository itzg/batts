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

<script src="${resourceurl}/js/jquery-1.7.2.js"></script>
<script src="${resourceurl}/js/jquery-ui-1.8.21.custom.min.js"></script>
<script src="${resourceurl}/js/jquery.validate.js"></script>
<script src="${resourceurl}/js/batts.js"></script>

</head>
<body>
    <div id="batteries">
        <h1>Batteries</h1>
        <div class="content"></div>
    </div>
    <div id="actions">
        <div id="availableChannel">
            <button id="btnAvailable">Available Batteries</button>
            <div id="addRemoveChoices" class="initiallyHidden">
                <img src="${resourceurl}/img/BtnSplitterTwoWay.png"
                    id="availableAddOrRemoveSplitter" />
                <div id="availableAddOrRemove">
                    <div>
                        <button id="btnAddMore">Add</button>
                    </div>
                    <div>
                        <button id="btnRemoveSome">Remove</button>
                    </div>
                </div>
            </div>
            <div id="battHowManyForm" class="initiallyHidden">
                <form method="post" action="#">
                        <label for="battAddRemoveCount">How many <span class="selectedBattType"></span>s</label>
                        <input size="3" type="text" name="count" id="battAddRemoveCount" value="2"></input>
                        <img class="imageButton submitter" alt="Submit" src="${resourceurl}/img/SubmitIcon.png">
                </form>
            </div>
        </div>
        <div id="devicesChannel">
            <button id="btnAddDevice">Add Device</button>
            <button id="btnOutIn" disabled="true">Out/In</button>
        </div>
    </div>
    <div id="devices">
        <h1>Devices</h1>
        <div id="deviceFiltering"></div>
        <div class="content"></div>
    </div>
    
    <div id="dialogs">
        <div id="dlgAddDevice" class="initiallyHidden">
            <form>
                <table>
                    <tr>
                        <td><label for="dlgAddDevice-label">Label</label></td>
                        <td><input name="label" id="dlgAddDevice-label"></input></td>
                    </tr>
                    <tr>
                        <td><label for="dlgAddDevice-desc" class="optional">Description</label></td>
                        <td><input name="description" id="dlgAddDevice-desc"></input></td>
                    </tr>
                    <tr>
                        <td><label>Needs</label></td>
                        <td>
                            <table>
                                <tr>
                                    <td>Count</td><td><input name="count" size="3" id="dlgAddDevice-count"></input></td>
                                </tr>
                                <tr>
                                    <td>Type</td>
                                    <td><select name="type">
                                        <option>AA</option>
                                        <option>AAA</option>
                                        <option>C</option>
                                        <option>D</option>
                                        <option>9V</option>
                                    </select></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <div>
                    <button type="submit" id="dlgAddDevice-btnAdd">Add</button>
                    <a href="#" id="dlgAddDevice-btnCancel">Cancel</a>
                </div>
            </form>
        </div>
        <div id="dlgError" class="initiallyHidden">
            <div id="dlgError-content"></div>
            <div><button id="dlgError-btnOK">OK</button></div>
        </div>
    </div>
</body>
</html>