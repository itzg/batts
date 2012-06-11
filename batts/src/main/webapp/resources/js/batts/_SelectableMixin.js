define(["dojo/_base/declare"], 
        function(declare) {
     
    return declare(null, {
    	/* PARMS */
    	selected: false,
    	
    	/* METHODS */
    	setSelected: function(/*bool*/ value) {
    		this.set('selected', value);
    	},
    	
        _setSelectedAttr: function(/*bool*/ value) {
        	if (value != this.selected) {
        		this.selected = value;
        		if (value) {
        			this.onSelected();
        		}
        		else {
        			this.onDeselected();
        		}
        		this.onSelectionChanged(this, value);
        	}
        },
        
        isSelected: function() {
        	return this.selected;
        },
    	
        /* EVENT HOOKS */
        onSelected: function() {},
        onDeselected: function() {},
        onSelectionChanged: function(source, newValue) {},
    });
});
