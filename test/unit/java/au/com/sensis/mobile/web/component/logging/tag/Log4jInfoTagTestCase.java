package au.com.sensis.mobile.web.component.logging.tag;

import org.apache.log4j.Level;

/**
 * Unit test {@link Log4jInfoTag}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class Log4jInfoTagTestCase extends AbstractLog4jTagTestCase {

    /**
     * @return
     */
    @Override
    protected final Log4jInfoTag createObjectUnderTest() {
        return new Log4jInfoTag();
    }

    @Override
    protected final String getOutputLogLevel() {
        return "INFO";
    }

    @Override
    protected final Level getLogLevel() {
        return Level.INFO;
    }

}
