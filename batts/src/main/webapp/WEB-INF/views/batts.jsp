<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Batts</title>

<%@ include file="/WEB-INF/pieces/common-head.jsp" %>

</head>
<body>
	<div id="banner">
		<button id="btnDoMore">More Things</button>
	</div>
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
            <button id="btnEditDevice" disabled="true">Edit Device</button>
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
        <div id="dlgEditDevice" class="initiallyHidden">
            <form action="#">
                <input type="hidden" name="deviceId" id="dlgEditDevice-device"/>
                <div class="dlgRow"><label for="dlgEditDevice-label">Label</label>
                    <input name="label" id="dlgEditDevice-label" type="text"/></div>
                <div class="dlgRow"><label for="dlgEditDevice-desc">Description</label>
                    <input name="description" id="dlgEditDevice-desc" type="text"/></div>
                <div class="buttonBar">
                <button type="button" id="dlgEditDevice-btnDel">Delete</button>
                <button type="submit" id="dlgEditDevice-btnSave">Save</button>
                    <a href="#" id="dlgEditDevice-cancel">Cancel</a>
                </div>
            </form>
        </div>
    </div>
    
    <div id="tooltips">
    	<div id="doMoreTooltip" class="initiallyHidden">
    		<button id="btnShareHousehold">Share Household</button>
    		<button id="btnLogout">Logout</button>
    	</div>
    </div>
</body>
</html>