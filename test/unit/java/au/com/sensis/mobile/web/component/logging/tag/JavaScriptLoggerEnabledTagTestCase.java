package au.com.sensis.mobile.web.component.logging.tag;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import au.com.sensis.mobile.web.component.logging.FeatureEnablementRegistry;
import au.com.sensis.mobile.web.component.logging.SimpleFeatureEnablementRegistryBean;
import au.com.sensis.mobile.web.component.logging.tag.JavaScriptLoggerEnabledTag.JavaScriptLoggerEnabledTagExtraInfo;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Base unit test case for {@link JavaScriptLoggerEnabledTag} and its derived classes.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class JavaScriptLoggerEnabledTagTestCase extends AbstractJUnit4TestCase {


    private static final String VAR_ATTRIBUTE_VALUE = "jsEnabledFlag";
    private JavaScriptLoggerEnabledTag objectUnderTest;
    private JavaScriptLoggerEnabledTagExtraInfo javaScriptLoggerEnabledTagExtraInfoUnderTest;
    private JspContext mockJspContext;
    private TagData mockTagData;
    private ServletContext mockServletContext;
    private WebApplicationContext mockWebApplicationContext;

    public JavaScriptLoggerEnabledTagTestCase() {
        super();
    }

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new JavaScriptLoggerEnabledTag());
        getObjectUnderTest().setJspContext(getMockJspContext());
        getObjectUnderTest().setVar(VAR_ATTRIBUTE_VALUE);

        setJavaScriptLoggerEnabledTagExtraInfoUnderTest(
                new JavaScriptLoggerEnabledTagExtraInfo());
    }

    @Test
    public void testDoTag() throws Throwable {

        EasyMock.expect(getMockJspContext().findAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(getMockWebApplicationContext());

        final FeatureEnablementRegistry featureEnablementRegistry =
            new SimpleFeatureEnablementRegistryBean();
        EasyMock.expect(getMockWebApplicationContext().getBean(
                JavaScriptLoggerEnabledTag.FEATURE_ENABLEMENT_REGISTRY_BEAN_ID,
                FeatureEnablementRegistry.class)).andReturn(featureEnablementRegistry);
        getMockJspContext().setAttribute(VAR_ATTRIBUTE_VALUE,
                featureEnablementRegistry.isJavaScriptLoggerEnabled());

        replay();

        getObjectUnderTest().doTag();

        verify();
    }

    /**
     * Test {@link JavaScriptLoggerEnabledTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testJavaScriptLoggerEnabledTagExtraInfoValidateWhenVarAttributeNull()
        throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("var"))
            .andReturn(null);

        EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
            getJavaScriptLoggerEnabledTagExtraInfoUnderTest().validate(getMockTagData());

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
     * Test {@link JavaScriptLoggerEnabledTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testJavaScriptLoggerEnabledTagExtraInfoValidateWhenValid() throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("var")).andReturn(
                "myVar");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getJavaScriptLoggerEnabledTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertNull("Number of validation messages is wrong",
                actualValidationMessages);
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
     * @param objectUnderTest the objectUnderTest to set
     */
    private void setObjectUnderTest(final JavaScriptLoggerEnabledTag objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the objectUnderTest
     */
    private JavaScriptLoggerEnabledTag getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @return the mockWebApplicationContext
     */
    public WebApplicationContext getMockWebApplicationContext() {
        return mockWebApplicationContext;
    }

    /**
     * @param mockWebApplicationContext the mockWebApplicationContext to set
     */
    public void setMockWebApplicationContext(
            final WebApplicationContext mockWebApplicationContext) {
        this.mockWebApplicationContext = mockWebApplicationContext;
    }

    /**
     * @return the mockServletContext
     */
    public ServletContext getMockServletContext() {
        return mockServletContext;
    }

    /**
     * @param mockServletContext the mockServletContext to set
     */
    public void setMockServletContext(final ServletContext mockServletContext) {
        this.mockServletContext = mockServletContext;
    }

    /**
     * @return the javaScriptLoggerEnabledTagExtraInfoUnderTest
     */
    public JavaScriptLoggerEnabledTagExtraInfo getJavaScriptLoggerEnabledTagExtraInfoUnderTest() {
        return javaScriptLoggerEnabledTagExtraInfoUnderTest;
    }

    /**
     * @param javaScriptLoggerEnabledTagExtraInfoUnderTest
     *            the javaScriptLoggerEnabledTagExtraInfoUnderTest to set
     */
    public void setJavaScriptLoggerEnabledTagExtraInfoUnderTest(
            final JavaScriptLoggerEnabledTagExtraInfo
                javaScriptLoggerEnabledTagExtraInfoUnderTest) {
        this.javaScriptLoggerEnabledTagExtraInfoUnderTest =
                javaScriptLoggerEnabledTagExtraInfoUnderTest;
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

}
