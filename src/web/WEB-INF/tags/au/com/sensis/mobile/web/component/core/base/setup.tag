<%@ tag body-content="empty" isELIgnored="false" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>
<%@ taglib prefix="crf" uri="/au/com/sensis/mobile/crf/crf.tld"%>

<%@ attribute name="device" required="true"
    type="au.com.sensis.wireless.common.volantis.devicerepository.api.Device"  
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
	var McsBrowser = function(bName, bPlatform, bversion, btel, bwtai) {
	    this.name = bName;
	    this.platform = bPlatform;
	    this.version = bversion;
	    this.telSupport = btel;
	    this.wtaiSupport = (bwtai != "" ) ? bwtai.split(" ") : this.wtaiSupport;
	}
	McsBrowser.prototype = {
	    name: new String(),
	    platform: new String(),
	    version: new String(), 
	    telSupport: new String(),
	    wtaiSupport: new Array()
	}
	
	var McsCustom = function(bPhone, imgCat) {
	    this.businessPhone = bPhone;
	    this.imageCategory = imgCat;
	}
	McsCustom.prototype = {
	    businessPhone: false,
	    imageCategory: new String("S")
	}
	
	var McsOutput = function(px, py) {
	    this.pixelsx = px;
	    this.pixelsy = py;
	}
	McsOutput.prototype = {
	    pixelsx: 120,
	    pixelsy: 120
	}
	
	var McsDevice = function(dname, dbrowser, dcustom, doutput) {
	    this.Name = dname;
	    this.Browser = dbrowser;
	    this.Custom = dcustom;
	    this.Output = doutput;
	}
	McsDevice.prototype = {
	    Name: new String(),
	    Browser: null,
	    Custom: null,
	    Output: null
	}
    var Device = new McsDevice(
        '${device.name}',
        
        new McsBrowser('<crf:deviceProperty device="${device}" property="brwsrname"/>',
                       '<crf:deviceProperty device="${device}" property="browser.platform"/>',
                       '<crf:deviceProperty device="${device}" property="brwsrvers"/>',
                       '<crf:deviceProperty device="${device}" property="dial.link.info"/>',
                       '<crf:deviceProperty device="${device}" property="UAProf.WtaiLibraries"/>'),
                     
        new McsCustom(<crf:deviceProperty device="${device}" property="custom.business.phone"/>,
                     '<crf:deviceProperty device="${device}" property="custom.imageCategory"/>'),
                   
        new McsOutput(${device.pixelsX}, ${device.pixelsY})
    );
</crf:script>
<logging:debug logger="${logger}" message="Exiting setup.tag" />
