<%@ tag body-content="empty" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>
<%@ taglib prefix="crf" uri="/au/com/sensis/mobile/crf/crf.tld"%>

<%@ attribute name="device" required="true"
    type="au.com.sensis.wireless.common.volantis.devicerepository.api.Device"  
    description="Device of the current user." %>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.base" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<base:compMcsBasePath var="compMcsBasePath" />

<crf:script src="comp/core/base/package" device="${device}" />

<crf:script name="deviceInfo" type="text/javascript" device="${context.device}">
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
