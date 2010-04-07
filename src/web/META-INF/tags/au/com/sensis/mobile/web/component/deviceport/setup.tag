<%@ tag body-content="empty" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="core" uri="/au/com/sensis/mobile/web/component/core/core.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/logging/logging.tld"%>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.deviceport" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<core:compMcsBasePath var="compMcsBasePath" />

<core:script src="${compMcsBasePath}/deviceport/scripts/deviceport-component.mscr"></core:script>

<logging:debug logger="${logger}" message="Exiting setup.tag" />
