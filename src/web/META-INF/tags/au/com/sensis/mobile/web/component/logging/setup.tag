<%@ tag body-content="empty" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="core" uri="/au/com/sensis/mobile/web/component/core/core.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/logging/logging.tld"%>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.logging" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<core:compMcsBasePath var="compMcsBasePath" />

<logging:jsLoggerEnabled var="javascriptLoggerEnabled"/>
<c:choose>
    <c:when test="${javascriptLoggerEnabled}">
        <logging:debug logger="${logger}" 
            message="javascriptLoggerEnabled is '${javascriptLoggerEnabled}'. Enabling JavaScript logger." />    
    
    	<%-- Setup components that we depend on. --%>
    	<core:setup />
    
    	<%-- Themes for current component. --%>
    	<%-- NOTE: none required --%>
    
    	<%-- Scripts for current component. --%>
    	<core:script src="${compMcsBasePath}/logging/scripts/logging-component.mscr"></core:script>
    
    </c:when>
    <c:otherwise>
        <logging:debug logger="${logger}" 
            message="javascriptLoggerEnabled is '${javascriptLoggerEnabled}'. Disabling JavaScript logger." />
    </c:otherwise>
</c:choose>

<logging:debug logger="${logger}" message="Exiting setup.tag" />
