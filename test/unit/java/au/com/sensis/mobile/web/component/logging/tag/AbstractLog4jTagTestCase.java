package au.com.sensis.mobile.web.component.logging.tag;

import java.io.StringWriter;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.mobile.web.component.logging.tag.Log4jTag.Log4jTagExtraInfo;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Base unit test case for {@link Log4jTag} and its derived classes.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public abstract class AbstractLog4jTagTestCase extends AbstractJUnit4TestCase {

    private static final String MESSAGE = "My Dummy MESSAGE";
    private static final String LOGGER = "My LOGGER name";
    private Log4jTag objectUnderTest;
    private PatternLayout simpleLayout;
    private WriterAppender writerAppender;
    private StringWriter stringWriter;
    private Logger expectedLogger;
    private TagData mockTagData;
    private Log4jTagExtraInfo log4jInfoExtraInfoUnderTest;

    public AbstractLog4jTagTestCase() {
        super();
    }

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        simpleLayout = new PatternLayout("%p : %m");
        stringWriter = new StringWriter();
        writerAppender = new WriterAppender(simpleLayout, stringWriter);

        expectedLogger = Logger.getLogger(LOGGER);
        expectedLogger.addAppender(writerAppender);
        expectedLogger.setLevel(getLogLevel());

        objectUnderTest = createObjectUnderTest();
        objectUnderTest.setMessage(MESSAGE);
        objectUnderTest.setLogger(expectedLogger);

        log4jInfoExtraInfoUnderTest = new Log4jTagExtraInfo();
    }

    protected abstract Log4jTag createObjectUnderTest();

    protected abstract String getOutputLogLevel();

    protected abstract Level getLogLevel();

    @Test
    public void testDoTagWhenLoggingEnabled() throws Throwable {
        objectUnderTest.doTag();

        Assert.assertEquals("log message is wrong", getOutputLogLevel() + " : " + MESSAGE,
                stringWriter.toString());

    }


    @Test
    public void testDoTagWhenLoggingNotEnabled() throws Throwable {
        expectedLogger.setLevel(Level.ERROR);

        objectUnderTest.doTag();

        Assert.assertEquals("log message is wrong", StringUtils.EMPTY,
                stringWriter.toString());

    }

    /**
     * Test {@link Log4jTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testLog4jInfoTagExtraInfoValidateWhenLoggerAttributeNull()
            throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("logger"))
                .andReturn(null);

        EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getLog4jInfoExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertEquals("Number of validation messages is wrong", 1,
                actualValidationMessages.length);

        final ValidationMessage actualValidationMessage =
                actualValidationMessages[0];
        final ValidationMessage expectedValidationMessage =
                new ValidationMessage("tagDataId",
                        "The logger attribute must not be null.");
        assertValidationMessagesEqual(expectedValidationMessage,
                actualValidationMessage);
    }

    /**
     * Test {@link Log4jTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testLog4jInfoTagExtraInfoValidateWhenValid() throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("logger")).andReturn(
                expectedLogger);

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getLog4jInfoExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertNull("Number of validation messages is wrong",
                actualValidationMessages);
    }

    /**
     * @param actualValidationMessage
     * @param expectedValidationMessage
     */
    private void assertValidationMessagesEqual(
            final ValidationMessage expectedValidationMessage,
            final ValidationMessage actualValidationMessage) {
        Assert.assertEquals("ValidationMessage has wrong id",
                expectedValidationMessage.getId(), actualValidationMessage
                        .getId());
        Assert.assertEquals("ValidationMessage has wrong message",
                expectedValidationMessage.getMessage(), actualValidationMessage
                        .getMessage());
    }

    /**
     * @return the mockTagData
     */
    public TagData getMockTagData() {
        return mockTagData;
    }

    /**
     * @param mockTagData the mockTagData to set
     */
    public void setMockTagData(final TagData mockTagData) {
        this.mockTagData = mockTagData;
    }

    /**
     * @return the log4jInfoExtraInfoUnderTest
     */
    public Log4jTagExtraInfo getLog4jInfoExtraInfoUnderTest() {
        return log4jInfoExtraInfoUnderTest;
    }

    /**
     * @param log4jInfoExtraInfoUnderTest
     *            the log4jInfoExtraInfoUnderTest to set
     */
    public void setLog4jInfoExtraInfoUnderTest(
            final Log4jTagExtraInfo log4jInfoExtraInfoUnderTest) {
        this.log4jInfoExtraInfoUnderTest = log4jInfoExtraInfoUnderTest;
    }

}
