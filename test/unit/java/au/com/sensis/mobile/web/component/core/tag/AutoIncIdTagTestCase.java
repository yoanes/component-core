package au.com.sensis.mobile.web.component.core.tag;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.mobile.web.component.core.tag.AutoIncIdTag.AutoIncIdTagExtraInfo;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link AutoIncIdTag}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class AutoIncIdTagTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_PREFIX = "myIdPrefix";
    private static final String DEFAULT_VAR = "myIdVarName";

    private AutoIncIdTag objectUnderTest;
    private AutoIncIdTagExtraInfo autoIncIdTagExtraInfoUnderTest;

    private JspContext mockJspContext;
    private TagData mockTagData;

    /**
     * Test setup.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new AutoIncIdTag());
        getObjectUnderTest().setJspContext(getMockJspContext());
        getObjectUnderTest().setPrefix(DEFAULT_PREFIX);
        getObjectUnderTest().setVar(DEFAULT_VAR);

        setAutoIncIdTagExtraInfoUnderTest(new AutoIncIdTagExtraInfo());
    }

    @Test
    public void testDoTagWhenNoPriorCounterExistsInRequest() throws Throwable {
        final Integer initialAutoIncCounterValue = new Integer(0);
        EasyMock.expect(
                getMockJspContext().getAttribute(
                        AutoIncIdTag.class.getName() + "." + DEFAULT_PREFIX,
                        PageContext.REQUEST_SCOPE)).andReturn(null);
        getMockJspContext().setAttribute(
                AutoIncIdTag.class.getName() + "." + DEFAULT_PREFIX,
                initialAutoIncCounterValue, PageContext.REQUEST_SCOPE);

        EasyMock.expect(
                getMockJspContext().getAttribute(
                        AutoIncIdTag.class.getName() + "." + DEFAULT_PREFIX,
                        PageContext.REQUEST_SCOPE)).andReturn(initialAutoIncCounterValue);
        getMockJspContext().setAttribute(DEFAULT_VAR, DEFAULT_PREFIX + "0");

        replay();

        getObjectUnderTest().doTag();

    }

    @Test
    public void testDoTagWhenPriorCounterExistsInRequest() throws Throwable {
        final Integer priorCounter = new Integer(10);
        EasyMock.expect(
                getMockJspContext().getAttribute(
                        AutoIncIdTag.class.getName() + "." + DEFAULT_PREFIX,
                        PageContext.REQUEST_SCOPE)).andReturn(priorCounter);
        final Integer updatedAutoIncCounter = new Integer(priorCounter.intValue() + 1);
        getMockJspContext().setAttribute(
                AutoIncIdTag.class.getName() + "." + DEFAULT_PREFIX,
                updatedAutoIncCounter, PageContext.REQUEST_SCOPE);

        EasyMock.expect(
                getMockJspContext().getAttribute(
                        AutoIncIdTag.class.getName() + "." + DEFAULT_PREFIX,
                        PageContext.REQUEST_SCOPE)).andReturn(updatedAutoIncCounter);
        getMockJspContext().setAttribute(DEFAULT_VAR, DEFAULT_PREFIX + updatedAutoIncCounter);

        replay();

        getObjectUnderTest().doTag();

    }

    /**
     * Test {@link AutoIncIdTag#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testAutoIncIdTagExtraInfoValidateWhenVarIsBlank()
            throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " " };

        for (final String testValue : testValues) {
            EasyMock.expect(getMockTagData().getAttributeString("var")).andReturn(
                    testValue);

            EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

            getHelper().replay();

            final ValidationMessage[] actualValidationMessages =
                    getAutoIncIdTagExtraInfoUnderTest().validate(
                            getMockTagData());

            Assert.assertEquals("Number of validation messages is wrong", 1,
                    actualValidationMessages.length);

            final ValidationMessage actualValidationMessage =
                    actualValidationMessages[0];
            final ValidationMessage expectedValidationMessage =
                    new ValidationMessage("tagDataId",
                            "You must set the var attribute to a non-blank value: '"
                                    + testValue + "'");
            assertValidationMessagesEqual(expectedValidationMessage,
                    actualValidationMessage);

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);
        }
    }

    /**
     * Test {@link AutoIncIdTag#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testAutoIncIdTagExtraInfoValidateWhenPrefixIsBlank()
    throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " " };

        for (final String testValue : testValues) {
            EasyMock.expect(getMockTagData().getAttributeString("var")).andReturn(
                    DEFAULT_VAR);
            EasyMock.expect(getMockTagData().getAttributeString("prefix")).andReturn(
                    testValue);

            EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

            getHelper().replay();

            final ValidationMessage[] actualValidationMessages =
                getAutoIncIdTagExtraInfoUnderTest().validate(
                        getMockTagData());

            Assert.assertEquals("Number of validation messages is wrong", 1,
                    actualValidationMessages.length);

            final ValidationMessage actualValidationMessage =
                actualValidationMessages[0];
            final ValidationMessage expectedValidationMessage =
                new ValidationMessage("tagDataId",
                        "You must set the prefix attribute to a non-blank value: '"
                        + testValue + "'");
            assertValidationMessagesEqual(expectedValidationMessage,
                    actualValidationMessage);

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);
        }
    }

    /**
     * Test {@link AutoIncIdTag#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testAutoIncIdTagExtraInfoValidateWhenValidate()
            throws Throwable {
        EasyMock.expect(getMockTagData().getAttributeString("var")).andReturn(
                DEFAULT_VAR);
        EasyMock.expect(getMockTagData().getAttributeString("prefix"))
                .andReturn(DEFAULT_PREFIX);

        EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getAutoIncIdTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertNull("ValidationMessages should be null",
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
     * @return the objectUnderTest
     */
    public AutoIncIdTag getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest
     *            the objectUnderTest to set
     */
    public void setObjectUnderTest(final AutoIncIdTag objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the mockJspContext
     */
    public JspContext getMockJspContext() {
        return mockJspContext;
    }

    /**
     * @param mockJspContext
     *            the mockJspContext to set
     */
    public void setMockJspContext(final JspContext mockJspContext) {
        this.mockJspContext = mockJspContext;
    }

    /**
     * @return the autoIncIdTagExtraInfoUnderTest
     */
    public AutoIncIdTagExtraInfo getAutoIncIdTagExtraInfoUnderTest() {
        return autoIncIdTagExtraInfoUnderTest;
    }

    /**
     * @param autoIncIdTagExtraInfoUnderTest
     *            the autoIncIdTagExtraInfoUnderTest to set
     */
    public void setAutoIncIdTagExtraInfoUnderTest(
            final AutoIncIdTagExtraInfo autoIncIdTagExtraInfoUnderTest) {
        this.autoIncIdTagExtraInfoUnderTest = autoIncIdTagExtraInfoUnderTest;
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
}
