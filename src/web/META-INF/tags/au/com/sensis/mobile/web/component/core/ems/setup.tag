<%@ tag body-content="empty" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.ems" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<base:compMcsBasePath var="compMcsBasePath" />

<%-- Scripts for current component. --%>
<base:script src="${compMcsBasePath}/core/ems/scripts/EMS.mscr"></base:script>
<base:script src="${compMcsBasePath}/core/ems/scripts/CommMode.mscr"></base:script>

<logging:debug logger="${logger}" message="Exiting setup.tag" />
