<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="core" uri="/au/com/sensis/mobile/web/component/core/core.tld"%>
<%@ taglib prefix="logging" uri="/au/com/sensis/mobile/web/component/logging/logging.tld"%>

<%--
  - Work around for Tomcat 5.0.28 to ensure that the JSP Expression Language is processed. 
  - Configuring this in web.xml using a jsp-property-group didn't seem to work (not sure why).
  - Should also work with Tomcat 6. 
  --%>
<%@ tag isELIgnored="false" %>

<logging:logger var="logger" name="au.com.sensis.mobile.web.component.core" />
<logging:info logger="${logger}" message="Entering setup.tag" />

<core:compMcsBasePath var="compMcsBasePath" />

<core:script src="${compMcsBasePath}/core/scripts/core-component.mscr"></core:script>

<core:script name="deviceInfo" type="text/javascript">
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
</core:script>

<logging:info logger="${logger}" message="Exiting setup.tag" />