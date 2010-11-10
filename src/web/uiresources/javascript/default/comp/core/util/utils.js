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
 *******************************************************************************************/

var Utilities = new Class({
	
	/**
	 * Separate delegate method to allow overriding by unit tests.
	 */
	getWindowLocation: function() {
		return window.location;
	},
	
	/**
	 * Returns the JSessionId appended to the current window URL, if any.
	 */	
	getJSessionId: function(){
		var path = new String(this.getWindowLocation());
		var start = path.indexOf(';');
		var sessionId = "";
		
		/* try to get from the location's URL */
		if (start > -1) {
			var paramStartIndex = path.indexOf("?");
			
			/**
			 * For nokia devices, if the anchor a user clicked on contains something like #myAnchor, 
			 * window.location may contain this anchor by the time this function is invoked.
			 * So we protect against it. 
			 **/
			var anchorStartIndex = path.indexOf("#");
			if (anchorStartIndex > -1){
				sessionId = path.substring(start, anchorStartIndex);
			} else if (paramStartIndex > -1) {
				sessionId = path.substring(start, paramStartIndex);
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
		if (!url.match(";jsessionid=")) {
			var sessid = this.getJSessionId();
			if (sessid != '') {
				var urlPrts = url.split("?");
				url = urlPrts[0] + sessid;
				if (urlPrts.length > 1) {
					url+= "?" + urlPrts[1];
				}
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