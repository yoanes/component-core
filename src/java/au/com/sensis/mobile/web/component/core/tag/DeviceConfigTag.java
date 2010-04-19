package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import au.com.sensis.mobile.web.component.core.device.DeviceConfigRegistry;
import au.com.sensis.wireless.common.volantis.devicerepository.api.Device;

/**
 * JSP tag handler for exposing the
 * {@link au.com.sensis.mobile.web.component.core.device.generated.AbstractDeviceConfig}
 * for a component's current user's device.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class DeviceConfigTag extends SimpleTagSupport {

    private String registryBeanName;
    private String var;
    private Device device;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = null;
        try {
            pageContext = (PageContext) getJspContext();
        } catch (final ClassCastException e) {
            throw new IllegalStateException(
                    "JspContext must be an instance of PageContext.", e);

        }
        final WebApplicationContext webApplicationContext =
                WebApplicationContextUtils
                        .getRequiredWebApplicationContext(pageContext
                                .getServletContext());
        final DeviceConfigRegistry deviceConfigRegistry =
                (DeviceConfigRegistry) webApplicationContext
                        .getBean(getRegistryBeanName());
        getJspContext().setAttribute(getVar(),
                deviceConfigRegistry.getDeviceConfig(getDevice()));
    }

    /**
     * Name of the variable to set the result into.
     *
     * @return Name of the variable to set the result into.
     */
    public String getVar() {
        return var;
    }

    /**
     * Name of the variable to set the result into.
     *
     * @param var Name of the variable to set the result into.
     */
    public void setVar(final String var) {
        this.var = var;
    }

    /**
     * @return the device
     */
    public Device getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(final Device device) {
        this.device = device;
    }

    /**
     * Prefix for the generated identifier.
     *
     * @return Prefix for the generated identifier.
     */
    public String getRegistryBeanName() {
        return registryBeanName;
    }

    /**
     * Prefix for the generated identifier.
     *
     * @param registryBeanName
     *            Prefix for the generated identifier.
     */
    public void setRegistryBeanName(final String registryBeanName) {
        this.registryBeanName = registryBeanName;
    }

    /**
     * {@link TagExtraInfo} implementation for validating the data set into the
     * {@link DeviceConfigTag}.
     */
    public static class DeviceConfigTagExtraInfo extends TagExtraInfo {

        /**
         * {@inheritDoc}
         */
        @Override
        public ValidationMessage[] validate(final TagData data) {
            final String varAttr = data.getAttributeString("var");
            if (StringUtils.isBlank(varAttr)) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(),
                        "You must set the var attribute to a non-blank value: '"
                                + varAttr + "'") };
            }

            final String registryBeanNameAttr = data.getAttributeString("registryBeanName");
            if (StringUtils.isBlank(registryBeanNameAttr)) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(),
                        "You must set the registryBeanName attribute to a non-blank value: '"
                                + registryBeanNameAttr + "'") };
            }

            return null;
        }

    }
}
