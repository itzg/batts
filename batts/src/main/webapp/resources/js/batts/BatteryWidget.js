define(["dojo/_base/declare", "dijit/_Widget", "dijit/_Templated", "dojo/text!./templates/BatteryWidget.html",
        "dojo/dom-class", "./_SelectableMixin"], 
        function(declare, _Widget, _Templated, template, domClass, _SelectableMixin) {
     
    return declare("batts.BatteryWidget", [_Widget, _Templated, _SelectableMixin], {
        templateString: template,
        baseClass: "battsBatteryWidget",
        
        available: 0,
        inuse: 0,
        type: "",
        
        /*transient*/ _over: false,
        
        typeNode: null,
        availableNode: null,
        inuseNode: null,
        outerNode: null,
        
        postCreate: function() {
    		this.inherited(arguments);
        	this.typeNode.innerHTML = this.type;
        	this.availableNode.innerHTML = this.available;
        	this.inuseNode.innerHTML = this.inuse;
        },
        
        _computeClass: function(suffix) {
        	return this.baseClass+suffix;
        },
        
        _onClick: function(evt) {
        	console.log("Type "+this.type+" got clicked");
        	this.set('selected', !this.selected);
        },
        
        _switchOuterClass: function(remove, add) {
    		domClass.remove(this.outerNode, this.baseClass+remove);
    		domClass.add(this.outerNode, this.baseClass+add);
        },
        
        _onMouseOver: function(evt) {
        	if (!this.selected) {
        		this._switchOuterClass("Off", "Over");
        	}
        },
        
        _onMouseOut: function(evt) {
        	if (!this.selected) {
        		this._switchOuterClass("Over", "Off");
        	}
        },
        
        onSelectionChanged: function(source, /*bool*/ newValue) {
        	console.log("BatteryWidget saw selection change to "+newValue);
        	if (newValue == false) {
        		this._switchOuterClass("On", "Off");
        	}
        	else {
        		domClass.remove(this.outerNode, this.baseClass+"Over "+this.baseClass+"Off");
        		domClass.add(this.outerNode, this.baseClass+"On");
        	}
        }
        //  your custom code goes here
    });
     
});