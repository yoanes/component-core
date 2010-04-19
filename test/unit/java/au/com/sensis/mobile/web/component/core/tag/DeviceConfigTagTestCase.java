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
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import au.com.sensis.mobile.web.component.core.device.DeviceConfigRegistry;
import au.com.sensis.mobile.web.component.core.device.generated.AbstractDeviceConfig;
import au.com.sensis.mobile.web.component.core.tag.DeviceConfigTag.DeviceConfigTagExtraInfo;
import au.com.sensis.wireless.common.volantis.devicerepository.api.Device;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link DeviceConfigTag}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class DeviceConfigTagTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_REGISTRY_BEAN_NAME
        = "clicktocall.comp.deviceConfigRegistry";
    private static final String DEFAULT_VAR = "devicConfig";

    private DeviceConfigTag objectUnderTest;
    private DeviceConfigTagExtraInfo deviceConfigTagExtraInfo;

    private PageContext mockPageContext;
    private JspContext mockJspContext;
    private TagData mockTagData;
    private MockServletContext springMockServletContext;
    private WebApplicationContext mockWebApplicationContext;
    private DeviceConfigRegistry mockDeviceConfigRegistry;
    private Device mockDevice;
    private AbstractDeviceConfig mockAbstractDeviceConfig;

    /**
     * Test setup.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new DeviceConfigTag());
        getObjectUnderTest().setJspContext(getMockPageContext());
        getObjectUnderTest().setRegistryBeanName(DEFAULT_REGISTRY_BEAN_NAME);
        getObjectUnderTest().setVar(DEFAULT_VAR);
        getObjectUnderTest().setDevice(getMockDevice());

        setDeviceConfigTagExtraInfo(new DeviceConfigTagExtraInfo());
        setSpringMockServletContext(new MockServletContext());
    }

    @Test
    public void testDoTagWhenSuccessful() throws Throwable {
        getSpringMockServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                getMockWebApplicationContext());
        EasyMock.expect(getMockPageContext().getServletContext()).andReturn(
                getSpringMockServletContext());

        EasyMock.expect(
                getMockWebApplicationContext().getBean(
                        DEFAULT_REGISTRY_BEAN_NAME)).andReturn(
                getMockDeviceConfigRegistry());

        EasyMock.expect(
                getMockDeviceConfigRegistry().getDeviceConfig(
                        getMockDevice())).andReturn(
                getMockAbstractDeviceConfig());

        getMockPageContext().setAttribute(DEFAULT_VAR,
                getMockAbstractDeviceConfig());

        replay();

        getObjectUnderTest().doTag();

        verify();

    }

    @Test
    public void testDoTagWhenJspContextIsNotPageContext() throws Throwable {
        getObjectUnderTest().setJspContext(getMockJspContext());

        replay();

        try {
            getObjectUnderTest().doTag();
            Assert.fail("IllegalStateException expected");
        } catch (final IllegalStateException e) {
            Assert.assertEquals("IllegalStateException has wrong message",
                    "JspContext must be an instance of PageContext.", e
                            .getMessage());
            Assert.assertNotNull("IllegalStateException should have a cause", e
                    .getCause());
            Assert
                    .assertTrue("IllegalStateException cause has wrong type",
                            ClassCastException.class.equals(e.getCause()
                                    .getClass()));
        }

        verify();
    }
    @Test
    public void testDoTagWhenNoRootWebApplicationContext() throws Throwable {
        EasyMock.expect(getMockPageContext().getServletContext()).andReturn(
                getSpringMockServletContext());

        replay();

        try {
            getObjectUnderTest().doTag();
            Assert.fail("IllegalStateException expected");
        } catch (final IllegalStateException e) {
            // No assertions. IllegalStateException message is created by the Spring framework.
        }

        verify();
    }

    @Test
    public void testDoTagWhenRegistryBeanCouldNotBeObtained() throws Throwable {
        getSpringMockServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                getMockWebApplicationContext());
        EasyMock.expect(getMockPageContext().getServletContext()).andReturn(
                getSpringMockServletContext());

        final NoSuchBeanDefinitionException expectedException =
                new NoSuchBeanDefinitionException("test");
        EasyMock.expect(
                getMockWebApplicationContext().getBean(
                        DEFAULT_REGISTRY_BEAN_NAME))
                .andThrow(expectedException);

        replay();

        try {
            getObjectUnderTest().doTag();
            Assert.fail("NoSuchBeanDefinitionException expected");
        } catch (final NoSuchBeanDefinitionException e) {
            // No assertions. IllegalStateException message is created by the
            // Spring framework.
        }

        verify();

    }

    /**
     * Test {@link DeviceConfigTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testDeviceConfigTagExtraInfoValidateWhenVarAttributeIsBlank()
            throws Throwable {

        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };

        for (final String testValue : testValues) {
            EasyMock.expect(getMockTagData().getAttributeString("var")).andReturn(
                    testValue);
            EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

            replay();

            final ValidationMessage[] actualValidationMessages =
                    getDeviceConfigTagExtraInfo().validate(getMockTagData());

            Assert.assertEquals("Number of validation messages is wrong", 1,
                    actualValidationMessages.length);

            final ValidationMessage actualValidationMessage =
                    actualValidationMessages[0];
            final ValidationMessage expectedValidationMessage =
                    new ValidationMessage("tagDataId",
                            "You must set the var attribute to a non-blank value: '"
                                    + testValue + "'");
            verify();

            assertValidationMessagesEqual(expectedValidationMessage,
                    actualValidationMessage);

            // Reset mocks prior to next iteration.
            reset();
            setReplayed(false);
        }
    }

    /**
     * Test {@link DeviceConfigTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testDeviceConfigTagExtraInfoValidateWhenRegistryBeanNameAttributeIsBlank()
    throws Throwable {

        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };

        for (final String testValue : testValues) {
            EasyMock.expect(getMockTagData().getAttributeString("var")).andReturn(
                    DEFAULT_VAR);
            EasyMock.expect(getMockTagData().getAttributeString("registryBeanName")).andReturn(
                    testValue);
            EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

            replay();

            final ValidationMessage[] actualValidationMessages =
                getDeviceConfigTagExtraInfo().validate(getMockTagData());

            Assert.assertEquals("Number of validation messages is wrong", 1,
                    actualValidationMessages.length);

            final ValidationMessage actualValidationMessage =
                actualValidationMessages[0];
            final ValidationMessage expectedValidationMessage =
                new ValidationMessage("tagDataId",
                        "You must set the registryBeanName attribute to a non-blank value: '"
                        + testValue + "'");
            verify();

            assertValidationMessagesEqual(expectedValidationMessage,
                    actualValidationMessage);

            // Reset mocks prior to next iteration.
            reset();
            setReplayed(false);
        }
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
     * Test {@link DeviceConfigTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testDeviceConfigTagExtraInfoValidateWhenValid()
        throws Throwable {
        EasyMock.expect(getMockTagData().getAttributeString("var")).andReturn(
                DEFAULT_VAR);
        EasyMock.expect(getMockTagData().getAttributeString("registryBeanName")).andReturn(
                DEFAULT_REGISTRY_BEAN_NAME);


        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
            getDeviceConfigTagExtraInfo().validate(getMockTagData());

        Assert.assertNull("validate returned wrong value",
                actualValidationMessages);
    }


    /**
     * @return the objectUnderTest
     */
    public DeviceConfigTag getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    public void setObjectUnderTest(final DeviceConfigTag objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the mockPageContext
     */
    public PageContext getMockPageContext() {
        return mockPageContext;
    }

    /**
     * @param mockPageContext the mockPageContext to set
     */
    public void setMockPageContext(final PageContext mockPageContext) {
        this.mockPageContext = mockPageContext;
    }

    /**
     * @return the springMockServletContext
     */
    public MockServletContext getSpringMockServletContext() {
        return springMockServletContext;
    }

    /**
     * @param springMockServletContext the springMockServletContext to set
     */
    public void setSpringMockServletContext(
            final MockServletContext springMockServletContext) {
        this.springMockServletContext = springMockServletContext;
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
     * @return the mockDeviceConfigRegistry
     */
    public DeviceConfigRegistry getMockDeviceConfigRegistry() {
        return mockDeviceConfigRegistry;
    }

    /**
     * @param mockDeviceConfigRegistry the mockDeviceConfigRegistry to set
     */
    public void setMockDeviceConfigRegistry(
            final DeviceConfigRegistry mockDeviceConfigRegistry) {
        this.mockDeviceConfigRegistry = mockDeviceConfigRegistry;
    }

    /**
     * @return the mockDevice
     */
    public Device getMockDevice() {
        return mockDevice;
    }

    /**
     * @param mockDevice the mockDevice to set
     */
    public void setMockDevice(final Device mockDevice) {
        this.mockDevice = mockDevice;
    }

    /**
     * @return the mockAbstractDeviceConfig
     */
    public AbstractDeviceConfig getMockAbstractDeviceConfig() {
        return mockAbstractDeviceConfig;
    }

    /**
     * @param mockAbstractDeviceConfig the mockAbstractDeviceConfig to set
     */
    public void setMockAbstractDeviceConfig(
            final AbstractDeviceConfig mockAbstractDeviceConfig) {
        this.mockAbstractDeviceConfig = mockAbstractDeviceConfig;
    }

    /**
     * @return the deviceConfigTagExtraInfo
     */
    public DeviceConfigTagExtraInfo getDeviceConfigTagExtraInfo() {
        return deviceConfigTagExtraInfo;
    }

    /**
     * @param deviceConfigTagExtraInfo the deviceConfigTagExtraInfo to set
     */
    public void setDeviceConfigTagExtraInfo(
            final DeviceConfigTagExtraInfo deviceConfigTagExtraInfo) {
        this.deviceConfigTagExtraInfo = deviceConfigTagExtraInfo;
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

}
