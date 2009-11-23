package au.com.sensis.mobile.web.component.logging.tag;

import org.apache.log4j.Level;

/**
 * Unit test {@link Log4jDebugTag}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class Log4jDebugTagTestCase extends AbstractLog4jTagTestCase {

    /**
     * @return
     */
    @Override
    protected final Log4jDebugTag createObjectUnderTest() {
        return new Log4jDebugTag();
    }

    @Override
    protected final String getOutputLogLevel() {
        return "DEBUG";
    }

    @Override
    protected final Level getLogLevel() {
        return Level.DEBUG;
    }

}
