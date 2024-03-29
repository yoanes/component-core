<%@ tag body-content="empty" isELIgnored="false" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>
<%@ taglib prefix="crf" uri="/au/com/sensis/mobile/crf/crf.tld"%>

<%@ attribute name="device" required="true"
    type="au.com.sensis.devicerepository.Device"  
    description="Device of the current user." %>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.util" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<crf:script src="comp/core/deviceport/deviceport.js" device="${device}" />

<logging:debug logger="${logger}" message="Exiting setup.tag" />
