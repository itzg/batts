///// GLOBALS /////////////////////////////////////////////////////////////////

var batteries = {
   selectedWidget: null,
   
   widgetsByType: {},
   
   howManyForm: null,
   
   getSelectedBatteryTypeKey:function() {
       return this.selectedWidget != null ? this.selectedWidget.data().batteryTypeKey :
           null;
   },
   
   getSelectedBatteryData: function() {
       return this.selectedWidget != null ? this.selectedWidget.data() :
           null;
   }
};

var devices = {
   selectedWidget: null,
   btnOutIn: null,
   
   getSelected: function(field) {
       return this.selectedWidget.data()[field];
   },

   deleteDevice: function(deviceWidget) {
       $.post(config.buildAjaxUrl("/household/api/deleteDevice"), 
               {id:deviceWidget.data().id}, 
               function(result){
                   devices.recreateDeviceWidgets(result);
               }
       );
   },
   
   recreateDeviceWidgets: function(data) {
       $("#devices > .content > *").remove();
       $.each(data, function(i,val){
           addDevice(val);
       });
       handleDeviceUnselected(this.selectedWidget);
   }
};

var dialogs = {
   addDevice: null,
   
   editDevice: null,
   
   createdShare: null,
   
   error: null,
   
   setup: function() {
       //// addDevice ////
       
       this.addDevice = $("#dlgAddDevice").dialog({
           autoOpen: false,
           modal: true,
           title: "Add Device",
           width: 400
       });
       $("#dlgAddDevice > form").validate({rules:{
           count: {required:true, min:1},
           label: {required:true, minlength:1}
       },
       submitHandler: function(form) {
           submitNewDevice(form);
           dialogs.addDevice.dialog("close");
       }
       });
       $("#dlgAddDevice-btnCancel").click(function(){
           dialogs.addDevice.dialog("close");
       });
       
       this.error = $("#dlgError").dialog({
           autoOpen: false,
           modal: true,
           title: "Error",
           width: 200
       });
       
       
       $("#dlgError-btnOK").click(function(){
           dialogs.error.dialog("close");
       });
       
       //// editDevice ////
       
       this.editDevice = $("#dlgEditDevice").dialog({
          autoOpen: false,
          modal: true,
          title: "Edit Device",
          width: 400,
          height: 190
       });
       
       function closeEditDlg() {
           dialogs.editDevice.dialog("close");
       }
       
       $("#dlgEditDevice > form").validate({rules:{
           label: {required:true, minlength:1}
       }, submitHandler: function(form) {
           $.post(config.buildAjaxUrl("/household/api/editDevice"), $(form).serialize(), function(result){
               devices.recreateDeviceWidgets(result);
           });
           closeEditDlg();
       }});
       
       $("#dlgEditDevice-btnDel").click(function() {
           closeEditDlg();
           if (devices.getSelected("using").count == 0) {
               devices.deleteDevice(devices.selectedWidget);
           }
           else {
               dialogs.showError("Cannot delete device since it still has batteries in-use");
           }
       });
       
       $("#dlgEditDevice-cancel").click(function(){
           closeEditDlg();
       });
       
       //// sharing ////
       
       this.createdShare = $("#dlgCreatedShare").dialog({
           autoOpen: false,
           modal: true,
           title: "Household Sharing",
           width: 400,
       });
       
       $("#dlgCreatedShare-OK").click(function(){
    	  dialogs.createdShare.dialog("close");
       });
   },
   
   showError: function(msg) {
       $("#dlgError-content").html(msg);
       this.error.dialog("open");
   },
   
   showEditDlg: function(deviceWidget) {
       $("#dlgEditDevice-device")[0].value = devices.getSelected("id");
       $("#dlgEditDevice-label")[0].value = devices.getSelected("label");
       $("#dlgEditDevice-desc")[0].value = devices.getSelected("description");

       this.editDevice.dialog("open");
   },
   
   showCreatedShareDlg: function(shareCode) {
	   $("#dlgCreatedShare-shareCode").html(shareCode);
	   this.createdShare.dialog("open");
   }
        
};

///// ENTRY ///////////////////////////////////////////////////////////////////

$(document).ready(function() {
    aspectSweep();
    
	initBatteriesPanel();
	initDevicesPanel();
	
	dialogs.setup();
	
	wireActions();
});

///// FUNCTIONS ///////////////////////////////////////////////////////////////

function aspectSweep() {
    $("button").button();
    $(".imageButton").button();
    $(".initiallyHidden").hide();

    $(".submitter").click(function(evt) {
        //console.log("submitter got clicked", evt);
        var formToSubmit = $(evt.target).parents("form").first();
        //console.log("about to submit", formToSubmit);
        formToSubmit.submit();
    });
}

function getSelectedBatteryWidget() {
    return $("#batteries .ui-selected").first();
}

function initBatteriesPanel() {
    var batteriesSelectable = $("#batteries > .content").selectable({filter:".battery",
        selected:function(event, ui) {
            //console.log("Event ",event,"ui", ui);
            batterySelectionChanged();
        },
        unselected: function(){
            batterySelectionChanged();
        }}
    );
    
    function batterySelectionChanged() {
        var selection = batteriesSelectable.find(".ui-selected");
        var selectedCount = selection.length;
        $("#btnAvailable").button('option', 'disabled', selectedCount == 0);
        if (selectedCount == 0) {
            adjustAvailableAddOrRemove(false);
            batteries.selectedWidget = null;
        }
        else if (selectedCount == 1) {
            batteries.selectedWidget = selection.first();
            $(".selectedBattType").html(batteries.getSelectedBatteryTypeKey());
        }
    };
    batterySelectionChanged();
    
    $.getJSON(config.buildAjaxUrl("/household/api/counts"), function(data) {
        $.each(data, function(i,val){
            //console.debug("Got "+val.batteryTypeKey+" with available="+val.available);
            
            var batteryWidget = $('<div class="battery ui-widget-content">'+
                    '<img src="'+config.resourceurl+'/img/'+val.batteryTypeKey+'-200dpi.png"/>'+
                    '<div class="detail">'+
                    '<div class="label">'+val.batteryTypeKey+'</div>'+
                    '<div class="counts"><span class="total">'+(val.available+val.inuse)+'</span> total, '+
                    '<span class="inuse">'+val.inuse+'</span> in use, '+
                    '<span class="available">'+val.available+'</span> available</div>'+
                    '</div>'+
                '</div>').data(val);
            batteries.widgetsByType[val.batteryTypeKey] = batteryWidget;
            batteryWidget.appendTo("#batteries .content");
        });
    });

}

function submitNewDevice(form) {
    $.post(config.buildAjaxUrl("/household/api/addDevice"),
            $(form).serialize(),
            function(data) {
        devices.recreateDeviceWidgets(data);
    }
    );
}

function updateDeviceBatteryCountStyle(deviceWidget) {
    var field = deviceWidget.find(".battery-count");
    field.toggleClass("full", deviceWidget.data().using.count > 0);
    field.toggleClass("empty", deviceWidget.data().using.count == 0);
}

function addDevice(deviceData) {
    var deviceWidget = $('<div class="device ui-widget-content">'+
            '<div class="label">'+deviceData.label+'</div>'+
            '<div class="description">'+(deviceData.description?deviceData.description:"")+'</div>'+
            '<div><span class="battery-count">'+deviceData.needs.count+' '+deviceData.needs.batteryTypeKey+'</span></div>'+
        '</div>').data(deviceData);
    updateDeviceBatteryCountStyle(deviceWidget);
    deviceWidget.appendTo("#devices .content");
}

function updateOutInButtonText(selected, takingOut) {
    if (selected) {
        devices.btnOutIn.find(".ui-button-text").html(takingOut ? "Take Out" : "Put In");
        devices.btnOutIn.button("enable");
    }
    else {
        devices.btnOutIn.find(".ui-button-text").html("In/Out");
        devices.btnOutIn.button("disable");
    }
}

function handleDeviceSelected(device) {
    devices.selectedWidget = $(device);
    updateOutInButtonText(true, devices.selectedWidget.data().using.count > 0);
    $("#btnEditDevice").button("enable");
}

function handleDeviceUnselected(device) {
    if ($("#devices > .content > .ui-selected").length == 0) {
        updateOutInButtonText(false);
        devices.selectedWidget = null;
        $("#btnEditDevice").button("disable");
    }
}

function initDevicesPanel() {
    var selectable = $("#devices > .content").selectable({filter:".device",
        selected:function(event, ui) {
            handleDeviceSelected(ui.selected);
        },
        unselected: function(event, ui){
            handleDeviceUnselected(ui.unselected);
        }}
    );
    
    $.getJSON(config.buildAjaxUrl("/household/api/devices"), function(data) {
        devices.recreateDeviceWidgets(data);
    });

}

function transferBatteriesWithDevice(device, battery, putIn) {
    var direction = putIn ? 1 : -1;
    $.post(config.buildAjaxUrl("/household/api/putInDevice"), { 
        batteryTypeKey: battery.data().batteryTypeKey,
        deviceId: device.data().id,
        count: direction * device.data().needs.count,
        }, function(result) {
            battery.data().available = result.available;
            battery.data().inuse = result.totalInUse;
            device.data().using.count = result.inDevice;
            
            updateBatteryWidgetCounts(battery);
            updateDeviceBatteryCountStyle(device);
            updateOutInButtonText(true, putIn);
        });
}

function shuffleBatteriesInfromDevice() {
    var device = devices.selectedWidget;
    var type = device.data().needs.batteryTypeKey;
    var battery = batteries.widgetsByType[type];
    if (battery) {
        if (device.data().using.count > 0) {
            // taking out
            transferBatteriesWithDevice(device, battery, false);
        }
        else {
            // putting in...check there's enough available
                var available = battery.data().available;
                var needsCount = device.data().needs.count;
                if (available >= needsCount) {
                    transferBatteriesWithDevice(device, battery, true);
                }
                else {
                    dialogs.showError("Not enough "
                            + type
                            + " batteries available. Needs " + needsCount
                                    + ", but only " + available + " available.");
                }
        }
    }
    else {
        //console.error("Couldn't find widget for battery type "+type);
    }
}

var availableSplitterShowing = false;
var availableCountShowing = false;
var availableAdding = null;

function adjustAvailableAddOrRemove(show) {
    if (show) {
        $("#addRemoveChoices").show();
    }
    else {
        $("#addRemoveChoices").hide();
        adjustHowManyForm(false);
    }
    availableSplitterShowing = show;
}

function adjustHowManyForm(showing, adding) {
    if (!availableSplitterShowing || showing == availableCountShowing) {
        return;
    }
    
    if (!showing) {
        $("#battHowManyForm").hide();
        availableCountShowing = false;
        $("#btnAddMore").button('option', 'disabled', false);
        $("#btnRemoveSome").button('option', 'disabled', false);
    }
    else {
        // Fix up the count field
        if (!adding) {
            // when removing, make sure it defaults to no more than currently available
            var field = $("#battAddRemoveCount")[0];
            var available = batteries.getSelectedBatteryData().available;
            if (field.value > available) {
                field.value = available;
            }
        }
        
        $("#battHowManyForm").show();
        availableCountShowing = true;
        $("#btnAddMore").button('option', 'disabled', !adding);
        $("#btnRemoveSome").button('option', 'disabled', adding);

        $("#battAddRemoveCount").select();
        
        availableAdding = adding;
    }
}

function updateBatteryWidgetCounts(battery) {
    var data = battery.data();
    battery.find(".counts > .available").html(data.available);
    battery.find(".counts > .total").html(data.available+data.inuse);
    battery.find(".counts > .inuse").html(data.inuse);
}

function requestHouseholdShare() {
	$.post(config.buildAjaxUrl("/household/api/share"), {}, function(/*HouseholdShare*/ result){
		dialogs.showCreatedShareDlg(result.shareCode);
	});
}

function wireActions() {
    $("#btnAvailable").click(function() {
        adjustAvailableAddOrRemove(!availableSplitterShowing);
    });
    
    $("#btnAddMore").click(function(){
        adjustHowManyForm(!availableCountShowing, true);
    });
    $("#btnRemoveSome").click(function(){
        adjustHowManyForm(!availableCountShowing, false);
    });
    
    batteries.howManyForm = $("#battHowManyForm > form");
    
    batteries.howManyForm.validate({
        rules:{
            count: "availableBattCount"
        },
        submitHandler: function(form) {
            $.post(config.buildAjaxUrl("/household/api/")+
                    (availableAdding ? "inc" : "dec")+
                    "Available/"+batteries.getSelectedBatteryTypeKey(), 
                    batteries.howManyForm.serialize(), function(newCount) {
                //console.log("got back", newCount);
                var widgetData = batteries.getSelectedBatteryData();
                widgetData.available = newCount;
                updateBatteryWidgetCounts(batteries.selectedWidget);
            });
            adjustAvailableAddOrRemove(false);
        }
    });
    $.validator.addMethod("availableBattCount", function(value, element){
        if (value >= 1) {
            return availableAdding ? true : value <= batteries.getSelectedBatteryData().available;
        }
        else {
            return false;
        }
    }, "Invalid");

    
    $("#btnAddDevice").click(function(){
        dialogs.addDevice.dialog("open");
    });
    
    devices.btnOutIn = $("#btnOutIn");
    devices.btnOutIn.click(function(){
        shuffleBatteriesInfromDevice();
    });
    
    $("#btnEditDevice").click(function(){
        dialogs.showEditDlg(devices.selectedWidget);
    });
    
    function hideDoMoreMenu() {
    	$("#btnDoMore").qtip('hide');
    }
    
    $("#btnDoMore").qtip({
    	content: $("#doMoreTooltip"),
    	position: {
    		my: 'top right',
    		at: 'bottom right',
    		viewport: $(window)
    	},
    	show: 'click',
    	hide: 'click',
    	style: {
    		classes: 'ui-tooltip-shadow'
    	}
    });
    
    $("#btnShareHousehold").click(function(){
    	hideDoMoreMenu();
    	requestHouseholdShare();
    });
    
    $("#btnLogout").click(function(){
    	hideDoMoreMenu();
    	window.location = config.buildUrl("/j_spring_security_logout");
    });
}
