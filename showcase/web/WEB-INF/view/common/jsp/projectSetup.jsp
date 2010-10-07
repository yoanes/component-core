<jsp:directive.include file="/WEB-INF/view/common/jsp/configInclude.jsp"/>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Set the default resource bundle for the current tag file. --%>    
<fmt:setBundle basename="project-environment" />    

<base:setup device="${context.device}"/>

<c:set var="emsJsUrl">
    <fmt:message key="env.ems.service.js" /><fmt:message key="env.ems.token" />
</c:set>
<ems:setup emsJsUrl="${emsJsUrl}" device="${context.device}"/>

<util:setup device="${context.device}"/>
<deviceport:setup device="${context.device}"/>
<logging:setup device="${context.device}"/>