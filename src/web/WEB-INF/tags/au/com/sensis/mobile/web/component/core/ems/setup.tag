<%@ tag body-content="empty" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>
<%@ taglib prefix="crf" uri="/au/com/sensis/mobile/crf/crf.tld"%>

<%@ attribute name="emsJsUrl" required="true"
    description="URL of the EMS JavaScript library." %>
<%@ attribute name="device" required="true"
    type="au.com.sensis.wireless.common.volantis.devicerepository.api.Device"  
    description="Device of the current user." %>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.ems" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<%-- Scripts for current component. --%>
<crf:script src="${emsJsUrl}" device="${device}" type="text/javascript"/>
<crf:script name="core-ems-communicationMode" type="text/javascript" device="${device}">
    EMS.Services.communicationMode = 'CrossDomain';
</crf:script>

<logging:debug logger="${logger}" message="Exiting setup.tag" />
