define(["dojo/_base/declare", "dijit/_Widget", "dijit/_Templated", "dojo/text!./templates/BatteryWidget.html",
        "dojo/dom-class"], 
        function(declare, _Widget, _Templated, template, domClass) {
     
    return declare("batts.BatteryWidget", [_Widget, _Templated], {
        templateString: template,
        baseClass: "battsBatteryWidget",
        
        available: 0,
        inuse: 0,
        type: "",
        
        selected: false,
        
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
        
        _setSelectedAttr: function(/*bool*/ value) {
        	if (this.selected && !value) {
        		this._switchOuterClass("On", "Off");
        	}
        	else if (!this.selected && value) {
        		domClass.remove(this.outerNode, this.baseClass+"Over "+this.baseClass+"Off");
        		domClass.add(this.outerNode, this.baseClass+"On");
        	}
        	
        	this._set("selected", value);
        }
        //  your custom code goes here
    });
     
});