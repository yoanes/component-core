<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/logging/logging.tld"%>

<%--
  - Work around for Tomcat 5.0.28 to ensure that the JSP Expression Language is processed. 
  - Configuring this in web.xml using a jsp-property-group didn't seem to work (not sure why).
  - Should also work with Tomcat 6. 
  --%>
<%@ tag isELIgnored="false" %>

<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false"
    description="Name of the variable in which to store the MCS policies base path for components."%>

<%@ variable name-from-attribute="var" alias="coreCompMcsBasePath" scope="AT_END" 
    description="Variable in which to store the MCS policies base path for components." %>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.core" />
<logging:debug logger="${logger}" message="Entering compMcsBasePath.tag" />

<%-- Set the default resource bundle for the current tag file. --%>    
<fmt:setBundle basename="au.com.sensis.mobile.web.component.core.core-component" />    

<%--
  - Set the output variable to the MCS policies base path for components.
  - 
  - NOTE: We could have chosen to implement a tag handler instead of a tag file but that seems
  - like overkill for such a simple thing. In particular, to be consistent with other Java code
  - in the component, you'd probably want the handler to access the property via the Spring 
  - context to get rather than loading the property bundle itself. Whereas in a tag file,
  - it is pretty standard to load a bundle. 
  --%>
<c:set var="coreCompMcsBasePath">
    <fmt:message key="comp.mcs.base.path"></fmt:message>
</c:set>

<logging:debug logger="${logger}" message="Exiting compMcsBasePath.tag" />