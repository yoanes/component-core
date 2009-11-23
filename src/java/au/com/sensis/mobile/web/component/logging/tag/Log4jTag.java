package au.com.sensis.mobile.web.component.logging.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.log4j.Logger;

/**
 * Facade to log4j.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public abstract class Log4jTag extends SimpleTagSupport {

    private String message;
    private String logger;

    /**
     * {@link TagExtraInfo} implementation for validating the data set into the
     * {@link Log4jInfoTag}.
     */
    public static class Log4jTagExtraInfo extends TagExtraInfo {

        /**
         * {@inheritDoc}
         */
        @Override
        public ValidationMessage[] validate(final TagData data) {
            final Object loggerAttr = data.getAttribute("logger");
            if (loggerAttr == null) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(), "The logger attribute must not be null.") };
            } else {
                return null;
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException, IOException {
        final Logger log4jLogger = Logger.getLogger(getLogger());
        logMessageIfRequired(log4jLogger);
    }

    /**
     * Subclasses should override this method to perform the logging
     * if required. As such, it is up to subclasses to determine the level
     * of logging.
     *
     * @param log4jLogger Logger to use for logging.
     */
    protected abstract void logMessageIfRequired(
            final Logger log4jLogger);

    /**
     * @return the message
     */
    public final String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public final void setMessage(final String message) {
        this.message = message;
    }

    /**
     * @return the logger
     */
    public final String getLogger() {
        return logger;
    }

    /**
     * @param logger the logger to set
     */
    public final void setLogger(final String logger) {
        this.logger = logger;
    }
}
