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
 * 	1.0		6/05/2009		Initial version released.		Tony Filipe, Yoanes Koesno
 *   
 *******************************************************************************************/

var Utilities = new Class({
	
	version: '1.0',
	
	/**
	 * Returns the JSessionId appended to the current window URL, if any.
	 */	
	getJSessionId: function(){
		var path = new String(window.location);
		var start = path.indexOf(';');
		var sessionId = "";
		
		/* try to get from the location's URL */
		if (start > -1) {
			var end = path.indexOf("?");
			if (end > -1) {
				sessionId = path.substring(start, end);
			} else {
				sessionId = path.substring(start);
			}
		}
		
		/* if nothing found in the current location, look from the cookie */
		else {
			var fromCookie = Cookie.read('JSESSIONID');
			if($defined(fromCookie)) sessionId = ';jsessionid=' + fromCookie;
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

var ReportingUtilities = new Class({
	Extends: Utilities,
	
	to: function(lUrl, optionalParams, optionalHeaders) {
		if($defined(lUrl) && lUrl.length > 0){		
			var ajaxReport = new MobileRequest({
				method: 'get',
				url: this.maintainSession(lUrl),
				onComplete: function() {}
			});
			
			var extraParams = new String();
			if($defined(optionalParams)){ 
				for(var param in optionalParams) {
					if(extraParams.length > 0) {
						extraParams += '&';
					}
					extraParams += param + '=' + optionalParams[param];
				}
			}
			
			if($defined(optionalHeaders)) 
				for(var attribute in optionalHeaders)
					ajaxReport.setHeader(attribute, optionalHeaders[attribute]);
					
			ajaxReport.send(extraParams);
		}
	}
});

var Reporting = new ReportingUtilities();

var BasicUtilities = new Class({
	
	swap: function(id1, id2) {
		if($(id1) && $(id2)) {
			var temp = $(id1).value;
			$(id1).value = $(id2).value;
			$(id2).value = temp;
		}
	}
});

var B = new BasicUtilities();