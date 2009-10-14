/*******************************************************************************************
 * SENSIS MOBILE WIDGETS
 * 
 * Version 1.0
 * 
 * SensisWidgets provides widget functions that can be used across mobile applications.
 * 
 * Available widget functions are:
 * - prepop: clears input text on focus and repopulates input text on blur 
 *           if text is not entered by the user.
 *
 * - swap:   exchange the values between 2 input fields on click of the detector element.
 *          
 * Note: These functions have intentionally been placed within a single Class 
 * to improve performance on mobile devices with limited memory. 
 * 
 * Dependencies:
 * - mootools.js
 * 
 * Change Log:
 * 	1.0		6/05/2009		Initial version released.		Tony Filipe, Joanes Koesno
 *   
 *******************************************************************************************/

var SensisWidgets = new Class({
	
	version: '1.0',

	/**
	 * Clears input text on focus and repopulates on blur if text is not entered.
	 */
    prepop: function(inputfield, text){
		
		$(inputfield).addEvent('focus', function() {
			if($(inputfield).value == text) {
				$(inputfield).value = '';
			}
		});

		$(inputfield).addEvent('blur', function() {
			if($(inputfield).value == '') {
				$(inputfield).value = text;
			}
		});
    },
    
    /**
     * Swap the values of 2 two input fields onclick of the detector
     */
     swap: function(field1, field2, detector) {
     
     	$(detector).addEvent('click', function() {
     		var temp = $(field1).value;
     		$(field1).value = $(field2).value;
     		$(field2).value = temp;
     	});
     }

});