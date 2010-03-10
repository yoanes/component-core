<%@ tag body-content="empty" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="core" uri="/au/com/sensis/mobile/web/component/core/core.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/logging/logging.tld"%>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.ems" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<core:compMcsBasePath var="compMcsBasePath" />

<%-- Scripts for current component. --%>
<%-- TODO: Openlayers to be removed after EMS prod drop on 12 Mar 2010. --%>
<core:script src="${compMcsBasePath}/ems/scripts/OpenLayers.mscr"></core:script>
<core:script src="${compMcsBasePath}/ems/scripts/EMS.mscr"></core:script>
<core:script src="${compMcsBasePath}/ems/scripts/CommMode.mscr"></core:script>

<logging:debug logger="${logger}" message="Exiting setup.tag" />
