<%@ tag body-content="empty" isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="base" uri="/au/com/sensis/mobile/web/component/core/base/base.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/core/logging/logging.tld"%>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.core" />
<logging:debug logger="${logger}" message="Entering setup.tag" />

<base:compMcsBasePath var="compMcsBasePath" />

<%-- TODO: add back if you decide that we do need this.--%>
<%--<base:script src="${compMcsBasePath}/core/scripts/viewport.mscr"></base:script>--%>

<base:script src="${compMcsBasePath}/core/base/scripts/base-component.mscr"></base:script>

<base:script name="deviceInfo" type="text/javascript">
    var Device = new McsDevice(
        '<pipeline:value-of expr="device:getDeviceName()"/>',
        
        new McsBrowser('<pipeline:value-of expr="device:getPolicyValue('brwsrname')"/>',
                       '<pipeline:value-of expr="device:getPolicyValue('browser.platform')"/>',
                       '<pipeline:value-of expr="device:getPolicyValue('brwsrvers')"/>',
                       '<pipeline:value-of expr="device:getPolicyValue('dial.link.info')"/>',
                       '<pipeline:value-of expr="device:getPolicyValue('UAProf.WtaiLibraries')"/>'),
                     
        new McsCustom(<pipeline:value-of expr="device:getPolicyValue('custom.business.phone')"/>,
                     '<pipeline:value-of expr="device:getPolicyValue('custom.imageCategory')"/>'),
                   
        new McsOutput(<pipeline:value-of expr="device:getPolicyValue('pixelsx')"/>,
                      <pipeline:value-of expr="device:getPolicyValue('pixelsy')"/>)
    );
</base:script>

<logging:debug logger="${logger}" message="Exiting setup.tag" />