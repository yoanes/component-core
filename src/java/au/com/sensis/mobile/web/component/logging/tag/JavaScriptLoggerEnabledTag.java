package au.com.sensis.mobile.web.component.logging.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.springframework.web.context.WebApplicationContext;

import au.com.sensis.mobile.web.component.logging.FeatureEnablementRegistry;

/**
 * Sets a flag to indicate if the JavaScript Logger is enabled or not and exposes the flag
 * to the page.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class JavaScriptLoggerEnabledTag extends SimpleTagSupport {

    /**
     * Name of the {@link Boolean} bean in the {@link WebApplicationContext}
     * that indicate if the JavaScript logger is enabled.
     */
    public static final String FEATURE_ENABLEMENT_REGISTRY_BEAN_ID
        = "core.comp.loggingFeatureEnablementRegistry";

    private String var;

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException, IOException {
        final WebApplicationContext webApplicationContext =
                (WebApplicationContext) getJspContext()
                        .findAttribute(
                                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        final FeatureEnablementRegistry featureEnablementRegistry =
                (FeatureEnablementRegistry) webApplicationContext.getBean(
                        FEATURE_ENABLEMENT_REGISTRY_BEAN_ID, FeatureEnablementRegistry.class);

        getJspContext().setAttribute(getVar(),
                featureEnablementRegistry.isJavaScriptLoggerEnabled());
    }

    /**
     * @return the var
     */
    public String getVar() {
        return var;
    }

    /**
     * @param var the var to set
     */
    public void setVar(final String var) {
        this.var = var;
    }

    /**
     * {@link TagExtraInfo} implementation for validating the data set into the
     * {@link JavaScriptLoggerEnabledTag}.
     */
    public static class JavaScriptLoggerEnabledTagExtraInfo extends TagExtraInfo {

        /**
         * {@inheritDoc}
         */
        @Override
        public ValidationMessage[] validate(final TagData data) {
            final Object varAttr = data.getAttribute("var");
            if (varAttr == null) {
                    return new ValidationMessage[] { new ValidationMessage(data
                            .getId(), "The var attribute must not be null.") };
            } else {
                return null;
            }

        }
    }

}
