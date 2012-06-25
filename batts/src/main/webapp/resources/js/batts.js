///// GLOBALS /////////////////////////////////////////////////////////////////

var batteries = {
   selectedWidget:null,
   howManyForm:null,
   howManyFormRules:{
       count: {required:true, min:1}
   },
   
   getSelectedBatteryTypeKey:function() {
       return this.selectedWidget != null ? this.selectedWidget.data().batteryTypeKey :
           null;
   },
   
   getSelectedBatteryData: function() {
       return this.selectedWidget != null ? this.selectedWidget.data() :
           null;
   }
};

var dialogs = {
   addDevice: null,
   
   setup: function() {
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
        console.log("submitter got clicked", evt);
        var formToSubmit = $(evt.target).parents("form").first();
        console.log("about to submit", formToSubmit);
        formToSubmit.submit();
    });
}

function getSelectedBatteryWidget() {
    return $("#batteries .ui-selected").first();
}

function initBatteriesPanel() {
    var batteriesSelectable = $("#batteries > .content").selectable({filter:".battery",
        selected:function(event, ui) {
            console.log("Event ",event,"ui", ui);
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
            console.debug("Got "+val.batteryTypeKey+" with available="+val.available);
            
            var batteryWidget = $('<div class="battery ui-widget-content">'+
                    '<img src="'+config.resourceurl+'/img/'+val.batteryTypeKey+'-200dpi.png"/>'+
                    '<div class="detail">'+
                    '<div class="label">'+val.batteryTypeKey+'</div>'+
                    '<div class="counts"><span class="total">'+(val.available+val.inuse)+'</span> total, '+
                    '<span class="inuse">'+val.inuse+'</span> in use, '+
                    '<span class="available">'+val.available+'</span> available</div>'+
                    '</div>'+
                '</div>').data(val);
            batteryWidget.appendTo("#batteries .content");
        });
    });

}

function submitNewDevice(form) {
    $.post(config.buildAjaxUrl("/household/api/addDevice"),
            $(form).serialize(),
            function(result) {
        addDevice(result);
    }
    );
}

function addDevice(deviceData) {
    var deviceWidget = $('<div class="device ui-widget-content">'+
            '<div class="label">'+deviceData.label+'</div>'+
            '<div class="description">'+(deviceData.description?deviceData.description:"")+'</div>'+
        '</div>').data(deviceData);
    deviceWidget.appendTo("#devices .content");
}

function initDevicesPanel() {
    var selectable = $("#devices > .content").selectable({filter:".device",
        selected:function(event, ui) {
            console.log("Event ",event,"ui", ui);
        },
        unselected: function(){
        }}
    );
    
    $.getJSON(config.buildAjaxUrl("/household/api/devices"), function(data) {
        $.each(data, function(i,val){
            console.debug("Got device",val);
            addDevice(val);
        });
    });

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
                console.log("got back", newCount);
                var widgetData = batteries.getSelectedBatteryData();
                widgetData.available = newCount;
                widgetData.total = newCount + widgetData.inuse;
                batteries.selectedWidget.find(".counts > .available").html(widgetData.available);
                batteries.selectedWidget.find(".counts > .total").html(widgetData.total);
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
}
