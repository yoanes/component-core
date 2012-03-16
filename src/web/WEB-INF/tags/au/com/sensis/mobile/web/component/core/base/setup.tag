<%@ tag body-content="empty" isELIgnored="false" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>
<%@ taglib prefix="crf" uri="/au/com/sensis/mobile/crf/crf.tld"%>

<%@ attribute name="device" required="true"
    type="au.com.sensis.devicerepository.Device"  
    description="Device of the current user." %>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.base" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<crf:script src="comp/core/base/package" device="${device}" />

<crf:script name="deviceInfo" type="text/javascript" device="${context.device}">

    <%-- 
      - Define these types inline and independent of mootools due to the ability of the
      - crf:bundleScripts (CRF-119) tag to shuffle the JavaScript order and place this block 
      - before the package above.
      --%>
	var DeviceRepositoryBrowser = function(bPlatform, bversion) {
	    this.platform = bPlatform;
	    this.version = bversion;
	}
	DeviceRepositoryBrowser.prototype = {
	    name: new String(),
	    platform: new String(),
	    version: new String()
	}
	
	var DeviceRepositoryCustom = function(bPhone, imgCat) {
	    this.businessPhone = bPhone;
	    this.imageCategory = imgCat;
	}
	DeviceRepositoryCustom.prototype = {
	    businessPhone: false,
	    imageCategory: new String("S")
	}
	
	var DeviceRepositoryOutput = function(px, py) {
	    this.pixelsx = px;
	    this.pixelsy = py;
	}
	DeviceRepositoryOutput.prototype = {
	    pixelsx: 120,
	    pixelsy: 120
	}
	
	var DeviceRepositoryDevice = function(dname, dbrowser, dcustom, doutput) {
	    this.Name = dname;
	    this.Browser = dbrowser;
	    this.Custom = dcustom;
	    this.Output = doutput;
	}
	DeviceRepositoryDevice.prototype = {
	    Name: new String(),
	    Browser: null,
	    Custom: null,
	    Output: null
	}
    var Device = new DeviceRepositoryDevice(
        '${device.name}',
        
        new DeviceRepositoryBrowser('<crf:deviceProperty device="${device}" property="mobile_browser"/>',
                       '<crf:deviceProperty device="${device}" property="mobile_browser_version"/>'),
                     
        new DeviceRepositoryCustom(<crf:deviceProperty device="${device}" property="custom.business.phone"/>,
                     '<crf:deviceProperty device="${device}" property="custom.imageCategory"/>'),
                   
        new DeviceRepositoryOutput(${device.pixelsX}, ${device.pixelsY})
    );
</crf:script>
<logging:debug logger="${logger}" message="Exiting setup.tag" />
