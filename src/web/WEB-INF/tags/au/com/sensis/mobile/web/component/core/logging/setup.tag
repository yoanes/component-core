<%@ tag body-content="empty" isELIgnored="false" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>
<%@ taglib prefix="crf" uri="/au/com/sensis/mobile/crf/crf.tld"%>

<%@ attribute name="device" required="true"
    type="au.com.sensis.devicerepository.Device"  
    description="Device of the current user." %>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.logging" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<logging:jsLoggerEnabled var="javascriptLoggerEnabled"/>
<c:choose>
    <c:when test="${javascriptLoggerEnabled}">
        <logging:debug logger="${logger}" 
            message="javascriptLoggerEnabled is '${javascriptLoggerEnabled}'. Enabling JavaScript logger." />    
    
    	<%-- Setup components that we depend on. --%>
    	<base:setup device="${device}" />
    
    	<%-- Themes for current component. --%>
    	<%-- NOTE: none required --%>
    
    	<%-- Scripts for current component. --%>
    	<crf:script src="comp/core/logging/package" device="${device}"/>
    
    </c:when>
    <c:otherwise>
        <logging:debug logger="${logger}" 
            message="javascriptLoggerEnabled is '${javascriptLoggerEnabled}'. Disabling JavaScript logger." />
    </c:otherwise>
</c:choose>

<logging:debug logger="${logger}" message="Exiting setup.tag" />
