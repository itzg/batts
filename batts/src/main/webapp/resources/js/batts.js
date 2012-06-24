///// GLOBALS /////////////////////////////////////////////////////////////////

var dialogs = {
        addMore: null,
        
        setup: function() {
            this.addMore = { 
                    dialog: $("#dlgAddMore").dialog({autoOpen: false,
                        modal:true,
                        width:400,
                        position: ['center','top']}),
                    form: null,
                    details: {widget:null, batteryTypeKey:null}, 
                    init: function() {
                        var me = this;
                        this.form = this.dialog.find("form");
                        this.form.validate({
                            rules:{
                                count: {required:true, min:1}
                            },
                            submitHandler: function(form) {
                                me.submit();
                            }
                        });
                        return this;
                    },
                    show: function(details) {
                        this.details = details;
                        console.log("About to show open on ", this.dialog);
                        this.reset();
                        this.dialog.dialog('open');
                    },
                    reset: function() {
                        this.dialog.dialog('option', 'title', "Add more "+this.details.batteryTypeKey+"s");
                    },
                    submit: function() {
                        console.log("in submit of", this);
                        var me = this;
                        $.post(config.baseurl+"/household/api/incAvailable/"+this.details.batteryTypeKey, 
                                this.form.serialize(), function(newCount) {
                            console.log("got back", newCount);
                            var widgetData = me.details.widget.data();
                            widgetData.available = newCount;
                            widgetData.total = newCount + widgetData.inuse;
                            me.details.widget.find(".counts > .available").html(widgetData.available);
                            me.details.widget.find(".counts > .total").html(widgetData.total);
                        });
                        this.dialog.dialog('close');
                    },
            }.init();
        }
};

var availableButtons = {
        
};

///// ENTRY ///////////////////////////////////////////////////////////////////

$(document).ready(function() {
    $("button").button();
    
	initBatteriesPanel();
	
	wireActions();
	
	dialogs.setup();
});

///// FUNCTIONS ///////////////////////////////////////////////////////////////

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
        var selectedCount = batteriesSelectable.find(".ui-selected").length;
        console.log("selected count is "+selectedCount);
        $("#btnAvailable").button('option', 'disabled', selectedCount == 0);
        if (selectedCount == 0) {
            adjustAvailableAddOrRemove(false);
        }
    };
    batterySelectionChanged();
    
    $.getJSON(config.baseurl+"/household/api/counts", function(data) {
        $.each(data, function(i,val){
            console.log("Got "+val.batteryTypeKey+" with available="+val.available);
            
            var batteryWidget = $('<div class="battery ui-widget-content">'+
                    '<img src="'+config.resourceurl+'/img/'+val.batteryTypeKey+'-200dpi.png"/>'+
                    '<div class="detail">'+
                    '<div class="label">'+val.batteryTypeKey+'</div>'+
                    '<div class="counts"><span class="total">'+(val.available+val.inuse)+'</span> total, '+
                    '<span class="inuse">'+val.inuse+'</span> in use, '+
                    '<span class="available">'+val.available+'</span> available</div>'+
                    '</div>'+
                '</div>').data(val);
            console.log(batteryWidget);
            batteryWidget.appendTo("#batteries .content");
        });
    });

}

var availableSplitterShowing = false;

function adjustAvailableAddOrRemove(show) {
    if (show) {
        $("#availableAddOrRemoveSplitter").show();
        $("#availableAddOrRemove").show();
        availableSplitterShowing = true;
    }
    else {
        $("#availableAddOrRemoveSplitter").hide();
        $("#availableAddOrRemove").hide();
        availableSplitterShowing = false;
    }
}

function wireActions() {
    $("#btnAddMore").click(function(){
        var selected = $("#batteries .ui-selected");
        if (selected.length > 0) {
            var widget = selected.first();
            console.log(widget.data());
            dialogs.addMore.show({widget:widget, batteryTypeKey:widget.data().batteryTypeKey});
        }
        else {
            console.log("None selected");
        }
    });
    
    adjustAvailableAddOrRemove(false);
    $("#btnAvailable").click(function() {
        adjustAvailableAddOrRemove(!availableSplitterShowing);
    });
}
