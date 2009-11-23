package au.com.sensis.mobile.web.component.logging.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.log4j.Logger;

/**
 * Sets a flag to indicate if the JavaScript Logger is enabled or not and exposes the flag
 * to the page.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class LoggerTag extends SimpleTagSupport {

    private String var;
    private String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException, IOException {
        getJspContext().setAttribute(getVar(),
                Logger.getLogger(getName()));
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * {@link TagExtraInfo} implementation for validating the data set into the
     * {@link LoggerTag}.
     */
    public static class LoggerTagExtraInfo extends TagExtraInfo {

        /**
         * {@inheritDoc}
         */
        @Override
        public ValidationMessage[] validate(final TagData data) {
            final Object nameAttr = data.getAttribute("name");
            final Object varAttr = data.getAttribute("var");
            if (nameAttr == null) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(), "The name attribute must not be null.") };
            } else if (varAttr == null) {
                    return new ValidationMessage[] { new ValidationMessage(data
                            .getId(), "The var attribute must not be null.") };
            } else {
                return null;
            }

        }
    }

}
