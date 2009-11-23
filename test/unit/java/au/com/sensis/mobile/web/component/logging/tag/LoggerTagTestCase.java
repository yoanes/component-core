package au.com.sensis.mobile.web.component.logging.tag;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.mobile.web.component.logging.tag.Log4jTag.Log4jTagExtraInfo;
import au.com.sensis.mobile.web.component.logging.tag.LoggerTag.LoggerTagExtraInfo;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link LoggerTag}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class LoggerTagTestCase extends AbstractJUnit4TestCase {


    private static final String LOGGER_NAME = "au.com.sensis.mobileweb.component.core";
    private static final String VAR_ATTRIBUTE_VALUE = "myLogger";
    private LoggerTag objectUnderTest;
    private LoggerTagExtraInfo loggerTagExtraInfoUnderTest;
    private JspContext mockJspContext;
    private TagData mockTagData;

    public LoggerTagTestCase() {
        super();
    }

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new LoggerTag());
        getObjectUnderTest().setJspContext(getMockJspContext());
        getObjectUnderTest().setVar(VAR_ATTRIBUTE_VALUE);
        getObjectUnderTest().setName(LOGGER_NAME);

        setLoggerTagExtraInfoUnderTest(new LoggerTagExtraInfo());
    }

    @Test
    public void testDoTag() throws Throwable {

        getMockJspContext().setAttribute(VAR_ATTRIBUTE_VALUE,
                Logger.getLogger(LOGGER_NAME));

        replay();

        getObjectUnderTest().doTag();

        verify();
    }

    /**
     * Test {@link LoggerTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testLoggerTagExtraInfoValidateWhenNameAttributeNull()
            throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("name"))
                .andReturn(null);
        EasyMock.expect(getMockTagData().getAttribute("var"))
            .andReturn("myVar");


        EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getLoggerTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertEquals("Number of validation messages is wrong", 1,
                actualValidationMessages.length);

        final ValidationMessage actualValidationMessage =
                actualValidationMessages[0];
        final ValidationMessage expectedValidationMessage =
                new ValidationMessage("tagDataId",
                        "The name attribute must not be null.");
        assertValidationMessagesEqual(expectedValidationMessage,
                actualValidationMessage);
    }

    /**
     * Test {@link LoggerTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testLoggerTagExtraInfoValidateWhenVarAttributeNull()
        throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("name"))
            .andReturn("au.com.sensis.mobile.web.component.core");
        EasyMock.expect(getMockTagData().getAttribute("var"))
            .andReturn(null);

        EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
            getLoggerTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertEquals("Number of validation messages is wrong", 1,
                actualValidationMessages.length);

        final ValidationMessage actualValidationMessage =
            actualValidationMessages[0];
        final ValidationMessage expectedValidationMessage =
            new ValidationMessage("tagDataId",
            "The var attribute must not be null.");
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
    public void testLoggerTagExtraInfoValidateWhenValid() throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("name")).andReturn(
                "my logger name");
        EasyMock.expect(getMockTagData().getAttribute("var")).andReturn(
                "myVar");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getLoggerTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertNull("Number of validation messages is wrong",
                actualValidationMessages);
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    private void setObjectUnderTest(final LoggerTag objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the objectUnderTest
     */
    private LoggerTag getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @return the mockJspContext
     */
    public JspContext getMockJspContext() {
        return mockJspContext;
    }

    /**
     * @param mockJspContext the mockJspContext to set
     */
    public void setMockJspContext(final JspContext mockJspContext) {
        this.mockJspContext = mockJspContext;
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
     * @return the loggerTagExtraInfoUnderTest
     */
    public LoggerTagExtraInfo getLoggerTagExtraInfoUnderTest() {
        return loggerTagExtraInfoUnderTest;
    }

    /**
     * @param loggerTagExtraInfoUnderTest the loggerTagExtraInfoUnderTest to set
     */
    public void setLoggerTagExtraInfoUnderTest(
            final LoggerTagExtraInfo loggerTagExtraInfoUnderTest) {
        this.loggerTagExtraInfoUnderTest = loggerTagExtraInfoUnderTest;
    }

}
