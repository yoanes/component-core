/**
 * Hacky unit test of utils.js.
 * TODO: hook up to a real unit testing framework like YuiTest !!!
 */

/* ========================================================================================== */
/* Quickly define some frameworky stuff. */
unittest = { };

unittest.Assert = {
		assertEquals: function (testId, message, expected, actual) {
			if (expected != actual) {
				throw new Error(message + ". Expected: [" 
						+ expected + "] but was: [" + actual + "]");
			}
		},

		logMessage: function (message) {
			var messageWrapper = new Element("p");
			messageWrapper.appendChild(document.createTextNode(message));
			$('testResults').appendChild(messageWrapper);
		}
}

var objectUnderTest = new Utilities();

/* ========================================================================================== */
//Run tests on load.
window.addEvent('load', function() {
	for (var currTest in unittest.tests) {
		try {
			unittest.tests[currTest].call();
			unittest.Assert.logMessage(currTest + " passed");
		} catch (e) {
			unittest.Assert.logMessage(currTest + " - " + e.toString());		
		}
	}
});

/* ========================================================================================== */
unittest.tests = {
	testGetJSessionIdWhenWindowLocationHasJSessionIdNoParams: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
				+ expectedJsessionId;
		}
		
		var actualJSessionId = objectUnderTest.getJSessionId();
		unittest.Assert.assertEquals("testGetJSessionIdWhenWindowLocationHasJSessionIdNoParams",
				"jsessionId wrong", expectedJsessionId, actualJSessionId);
	},

	testGetJSessionIdWhenWindowLocationHasJSessionIdNoParamsWhenContainsAnchor: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
				+ expectedJsessionId + "#map";
		}
		
		var actualJSessionId = objectUnderTest.getJSessionId();
		unittest.Assert.assertEquals("testGetJSessionIdWhenWindowLocationHasJSessionIdNoParamsWhenContainsAnchor", 
				"jsessionId wrong", expectedJsessionId, actualJSessionId);
	},
	
	testGetJSessionIdWhenWindowLocationHasJSessionIdAndParams: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
				+ expectedJsessionId + "?param1=blah";
		}
		
		var actualJSessionId = objectUnderTest.getJSessionId();
		unittest.Assert.assertEquals("testGetJSessionIdWhenWindowLocationHasJSessionIdAndParams",
				"jsessionId wrong", expectedJsessionId, actualJSessionId);
	},	
	
	testGetJSessionIdWhenWindowLocationHasJSessionIdAndParamsAndContainsAnchor: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
				+ expectedJsessionId + "#map" + "?param1=blah";
		}
		
		var actualJSessionId = objectUnderTest.getJSessionId();
		unittest.Assert.assertEquals("testGetJSessionIdWhenWindowLocationHasJSessionIdAndParams",
				"jsessionId wrong", expectedJsessionId, actualJSessionId);
	},	
	
	testGetJSessionIdWhenWindowLocationHasNoJSessionIdButCookieDoes: function() {
		var expectedJsessionIdValue = "8F063ED12FFD1812A668CE52A18D81F9";
		var expectedJsessionId = ";jsessionid=" + expectedJsessionIdValue;
		Cookie.write("JSESSIONID", expectedJsessionIdValue);
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
				+ "?param1=blah";
		}
		
		var actualJSessionId = objectUnderTest.getJSessionId();
		unittest.Assert.assertEquals("testGetJSessionIdWhenWindowLocationHasNoJSessionIdButCookieDoes",
				"jsessionId wrong", expectedJsessionId, actualJSessionId);
	},	
	
	testMaintainSessionWhenUrlHasNoParamsAndNoJsessionId: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
				+ expectedJsessionId + "?param1=blah";
		}
		
		var url = "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action";
		var expectedUrl = url + expectedJsessionId;
		var actualUrl = objectUnderTest.maintainSession(url);
		unittest.Assert.assertEquals("testMaintainSessionWhenUrlHasNoParamsAndNoJsessionId",
				"jsessionId wrong", expectedUrl, actualUrl);
	},
	
	testMaintainSessionWhenUrlHasParamsAndNoJsessionId: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
			+ expectedJsessionId + "?param1=blah";
		}
		
		var urlWithoutParams = "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action";
		var urlParams = "?something=somethingValue&something2=something2Value";
		var url = urlWithoutParams + urlParams;
		var expectedUrl = urlWithoutParams + expectedJsessionId + urlParams;
		var actualUrl = objectUnderTest.maintainSession(url);
		unittest.Assert.assertEquals("testMaintainSessionWhenUrlHasNoParamsAndNoJsessionId",
				"jsessionId wrong", expectedUrl, actualUrl);
	},
	
	testMaintainSessionWhenUrlHasParamsAndJsessionId: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
			+ expectedJsessionId + "?param1=blah";
		}
		
		var urlWithoutParams = "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action;jsessionid=9187917492749217";
		var urlParams = "?something=somethingValue&something2=something2Value";
		var url = urlWithoutParams + urlParams;
		var expectedUrl = url;
		var actualUrl = objectUnderTest.maintainSession(url);
		unittest.Assert.assertEquals("testMaintainSessionWhenUrlHasNoParamsAndNoJsessionId",
				"jsessionId wrong", expectedUrl, actualUrl);
	},
	
	testMaintainSessionWhenUrlHasNoParamsAndJsessionId: function() {
		var expectedJsessionId = ";jsessionid=8F063ED12FFD1812A668CE52A18D81F9";
		objectUnderTest.getWindowLocation = function() {
			return "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action" 
			+ expectedJsessionId + "?param1=blah";
		}
		
		var url = "http://dsb.sensis.com.au/uidev/map-component-showcase/map/getPois.action;jsessionid=9187917492749217";
		var expectedUrl = url;
		var actualUrl = objectUnderTest.maintainSession(url);
		unittest.Assert.assertEquals("testMaintainSessionWhenUrlHasNoParamsAndNoJsessionId",
				"jsessionId wrong", expectedUrl, actualUrl);
	}
}