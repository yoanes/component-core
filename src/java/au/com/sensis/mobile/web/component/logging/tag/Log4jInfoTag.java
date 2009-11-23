package au.com.sensis.mobile.web.component.logging.tag;

import org.apache.log4j.Logger;

/**
 * Facade to log4j's info level logging.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class Log4jInfoTag extends Log4jTag {

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void logMessageIfRequired(final Logger log4jLogger) {
        if (log4jLogger.isInfoEnabled()) {
            log4jLogger.info(getMessage());
        }
    }
}
