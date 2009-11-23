<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="core" uri="/au/com/sensis/mobile/web/component/core/core.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/logging/logging.tld"%>

<%--
  - Work around for Tomcat 5.0.28 to ensure that the JSP Expression Language is processed. 
  - Configuring this in web.xml using a jsp-property-group didn't seem to work (not sure why). 
  - Should also work with Tomcat 6.  
  --%>
<%@ tag isELIgnored="false" %>

<%@ attribute name="baseCompMcsPath" required="true" type="java.lang.String"%>

<logging:jsLoggerEnabled var="javascriptLoggerEnabled"/>

<logging:info logger="au.com.sensis.mobile.web.component.logging" 
    message="Entering setup.tag" />

<c:choose>
    <c:when test="${javascriptLoggerEnabled}">
        <logging:debug logger="au.com.sensis.mobile.web.component.logging" 
            message="javascriptLoggerEnabled is '${javascriptLoggerEnabled}'. Enabling JavaScript logger." />    
    
    	<%-- Setup components that we depend on. --%>
    	<core:setup baseCompMcsPath="${baseCompMcsPath}"/>
    
    	<%-- Themes for current component. --%>
    	<%-- NOTE: none required --%>
    
    	<%-- Scripts for current component. --%>
    	<core:script src="${baseCompMcsPath}/logging/scripts/logging-component.mscr"></core:script>
    
    </c:when>
    <c:otherwise>
        <logging:debug logger="au.com.sensis.mobile.web.component.logging" 
            message="javascriptLoggerEnabled is '${javascriptLoggerEnabled}'. Disabling JavaScript logger." />
    </c:otherwise>
</c:choose>

<logging:info logger="au.com.sensis.mobile.web.component.logging" message="Exiting setup.tag" />
