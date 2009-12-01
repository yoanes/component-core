/*******************************************************************************************
 * SENSIS MOBILES COMMON UTILITIES
 * 
 * Version 1.0
 * 
 * Provides common Javscript functions that can be used across mobile applications.
 * 
 * Note: These functions have intentionally been placed within a single Class 
 * to improve performance on mobile devices with limited memory. 
 * 
 * Dependencies:
 * - mootools.js
 * 
 * Change Log:
 * 	1.0		6/05/2009		Initial version released.		Tony Filipe
 *   
 *******************************************************************************************/

var SensisUtils = new Class({
	
	version: '1.0',
	
	/**
	 * Returns the JSessionId appended to the current window URL, if any.
	 */	
	getJSessionId: function(){

		var path = new String(window.location);
		var start = path.indexOf(';');
		var sessionId = "";
		if (start > -1) {
			var end = path.indexOf("?");
			if (end > -1) {
				sessionId = path.substring(start, end);
			} else {
				sessionId = path.substring(start);
			}
		}
		return sessionId;
	},

	/**
	 * Appends the JSessionId (if one exists) to the given URL, maintaining any get parameters.
	 * 
	 * @param url The URL to append the session id to
	 * @return The URL with session id appended (if one exists)
	 */
	maintainSession: function(url) {
		var sessid = this.getJSessionId();
		if (sessid != '') {
			var urlPrts = url.split("?");
			url = urlPrts[0] + sessid;
			if (urlPrts.length > 1) {
				url+= "?" + urlPrts[1];
			}
		}
		return url;
	}
	
});


