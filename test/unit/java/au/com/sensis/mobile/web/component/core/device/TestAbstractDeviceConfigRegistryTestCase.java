package au.com.sensis.mobile.web.component.core.device;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.mobile.web.component.core.device.generated.AbstractDeviceConfig;
import au.com.sensis.mobile.web.component.core.device.generated.DeviceConfigs;
import au.com.sensis.mobile.web.component.core.device.generated.IdentifiedDeviceConfig;
import au.com.sensis.mobile.web.component.core.device.generated.UserAgentDeviceConfig;
import au.com.sensis.wireless.common.utils.jaxb.XMLBinder;
import au.com.sensis.wireless.common.volantis.devicerepository.api.Device;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link AbstractDeviceConfigRegistry}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class TestAbstractDeviceConfigRegistryTestCase extends
        AbstractJUnit4TestCase {

    private static final String DEVICE_CONFIG_FILE
        = "/au/com/sensis/mobile/web/component/core/device/test-device-config.xml";

    private static final String APPLE_IPHONE_DEVICE_ID = "Apple-iphone";
    private static final String APPLE_IPHONE_PARENT_DEVICE_ID = "Apple";
    private static final String LEAF_NODE_DEVICE_ID2 = "device2";
    private static final String DEVICE_ID_NOT_FOUND = "notFoundId";
    private static final String DEVICE_ID_NOT_FOUND_PARENT = "parentNotFoundId";

    private static final String IPHONE_OS_VERSION_3_0_USER_AGENT =
        "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ "
        + "(KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3";
    private static final String IPHONE_OS_VERSION_2_1_USER_AGENT =
        "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ "
        + "(KHTML, like Gecko) Version/2.1 Mobile/1A543a Safari/419.3";
    private static final String IPHONE_OS_VERSION_1_5_USER_AGENT =
        "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ "
        + "(KHTML, like Gecko) Version/1.5 Mobile/1A543a Safari/419.3";

    private XMLBinder mockXmlBinder;

    private File deviceConfigXmlFile;

    private DeviceConfigRegistry objectUnderTest;

    private Device mockDevice;
    private DeviceConfig deviceConfig1;
    private DeviceConfig userAgentDeviceConfigIphoneOSVersion1Or2;
    private DeviceConfig appleIphoneDeviceConfig;
    private DeviceConfig defaultDeviceConfig;
    private AbstractDeviceConfig mockDeviceConfig;

    /**
     * Setup test data.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setDefaultDeviceConfig(new DeviceConfig());
        setDeviceConfig1(new DeviceConfig());
        setAppleIphoneDeviceConfig(new DeviceConfig());
        setUserAgentDeviceConfigIphoneOSVersion1Or2(new DeviceConfig());
    }

    /**
     * Test getDeviceConfig when an IdentifiedDeviceConfig is found
     * but it has no UserAgentDeviceConfigs.
     */
    @Test
    public void
        testGetDeviceConfigWhenIdentifiedDeviceConfigFoundAndNoUserAgentDeviceConfigExists()
            throws Throwable {

        final DeviceConfigs expectedDeviceConfigs =
                createValidDeviceConfigsContainingOnlyLeafNodeDeviceIds();
        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        EasyMock.expect(getMockDevice().getDeviceNameHierarchyIterator())
                .andReturn(createValidDeviceNameHierarchyIterator())
                .atLeastOnce();

        // Expectation to cover off potential debug logging.
        EasyMock.expect(getMockDevice().getName()).andReturn(
                APPLE_IPHONE_DEVICE_ID).anyTimes();

        replay();

        // Due to the constructor invoking mocks, we must instantiate
        // the object under test in this method instead of in the setup method.
        setObjectUnderTest(new DeviceConfigRegistry(DEVICE_CONFIG_FILE,
                getMockXmlBinder(), getDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig = (DeviceConfig)
                getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertSame("Wrong deviceConfig returned", getAppleIphoneDeviceConfig(),
                actualDeviceConfig);

    }

    /**
     * Test getDeviceConfig when an IdentifiedDeviceConfig is found
     * and it has a UserAgentDeviceConfig that matches.
     */
    @Test
    public void testGetDeviceConfigWhenIdentifiedDeviceConfigAndUserAgentDeviceConfigFound()
        throws Throwable {

        final DeviceConfigs expectedDeviceConfigs =
            createValidDeviceConfigsContainingIPhoneOSVersion1Or2UserAgentConfig();
        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        EasyMock.expect(getMockDevice().getDeviceNameHierarchyIterator())
            .andReturn(createValidDeviceNameHierarchyIterator())
            .atLeastOnce();

        EasyMock.expect(getMockDevice().getUserAgent()).andReturn(
                IPHONE_OS_VERSION_1_5_USER_AGENT).atLeastOnce();

        // Expectation to cover off potential debug logging.
        EasyMock.expect(getMockDevice().getName()).andReturn(
                APPLE_IPHONE_DEVICE_ID).anyTimes();


        replay();

        // Due to the constructor invoking mocks, we must instantiate
        // the object under test in this method instead of in the setup method.
        setObjectUnderTest(new DeviceConfigRegistry(DEVICE_CONFIG_FILE,
                getMockXmlBinder(), getDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig = (DeviceConfig)
        getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertSame("Wrong deviceConfig returned",
                getUserAgentDeviceConfigIphoneOSVersion1Or2(),
                actualDeviceConfig);

    }

    /**
     * Test getDeviceConfig when an IdentifiedDeviceConfig is found
     * and it has UserAgentDeviceConfigs but they do not match.
     */
    @Test
    public void testGetDeviceConfigWhenIdentifiedDeviceConfigAndUserAgentDeviceConfigNotFound()
        throws Throwable {

        final DeviceConfigs expectedDeviceConfigs =
            createValidDeviceConfigsContainingIPhoneOSVersion1Or2UserAgentConfig();
        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        EasyMock.expect(getMockDevice().getDeviceNameHierarchyIterator())
        .andReturn(createValidDeviceNameHierarchyIterator())
        .atLeastOnce();

        EasyMock.expect(getMockDevice().getUserAgent()).andReturn(
                IPHONE_OS_VERSION_3_0_USER_AGENT).atLeastOnce();

        // Expectation to cover off potential debug logging.
        EasyMock.expect(getMockDevice().getName()).andReturn(
                APPLE_IPHONE_DEVICE_ID).anyTimes();


        replay();

        // Due to the constructor invoking mocks, we must instantiate
        // the object under test in this method instead of in the setup method.
        setObjectUnderTest(new DeviceConfigRegistry(DEVICE_CONFIG_FILE,
                getMockXmlBinder(), getDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig = (DeviceConfig)
        getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertSame("Wrong deviceConfig returned",
                getAppleIphoneDeviceConfig(),
                actualDeviceConfig);

    }

    @Test
    public void testGetDeviceConfigWhenParentFoundAndNoUserAgentDeviceConfig()
        throws Throwable {

        final DeviceConfigs expectedDeviceConfigs =
                createValidDeviceConfigsContainingParentDeviceId();
        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        EasyMock.expect(getMockDevice().getDeviceNameHierarchyIterator())
                .andReturn(createValidDeviceNameHierarchyIterator())
                .atLeastOnce();

        // Expectation to cover off potential debug logging.
        EasyMock.expect(getMockDevice().getName()).andReturn(
                APPLE_IPHONE_DEVICE_ID).anyTimes();

        replay();

        // Due to the constructor invoking mocks, we must instantiate
        // the object under test in this method instead of in the setup method.
        setObjectUnderTest(new DeviceConfigRegistry(DEVICE_CONFIG_FILE,
                getMockXmlBinder(), getDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig = (DeviceConfig)
                getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertSame("message", expectedDeviceConfigs
                .getIdentifiedDeviceConfig().get(0).getDeviceConfig(),
                actualDeviceConfig);

    }

    private Iterator<String> createValidDeviceNameHierarchyIterator() {
        return Arrays.asList(APPLE_IPHONE_DEVICE_ID, APPLE_IPHONE_PARENT_DEVICE_ID).iterator();
    }

    @Test
    public void testGetDeviceConfigWhenNotFound() throws Throwable {

        final DeviceConfigs expectedDeviceConfigs
            = createValidDeviceConfigsContainingOnlyLeafNodeDeviceIds();
        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        EasyMock.expect(getMockDevice().getDeviceNameHierarchyIterator()).andReturn(
                Arrays.asList(DEVICE_ID_NOT_FOUND, DEVICE_ID_NOT_FOUND_PARENT).iterator())
                    .atLeastOnce();

        // Expectation to cover off potential debug logging.
        EasyMock.expect(getMockDevice().getName()).andReturn(
                DEVICE_ID_NOT_FOUND).anyTimes();

        replay();

        // Due to the constructor invoking mocks, we must instantiate
        // the object under test in this method instead of in the setup method.
        setObjectUnderTest(new DeviceConfigRegistry(DEVICE_CONFIG_FILE,
                getMockXmlBinder(), getDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig = (DeviceConfig)
            getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertEquals("actualDeviceConfig is wrong", getDefaultDeviceConfig(),
                actualDeviceConfig);

    }

    @Test
    public void testConstructionWhenDeviceConfigContainsDuplicateDeviceId()
            throws Throwable {
        final DeviceConfigs expectedDeviceConfigs =
                createDeviceConfigsContainingDuplicateDeviceId();
        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        replay();

        try {
            new DeviceConfigRegistry(DEVICE_CONFIG_FILE, getMockXmlBinder(),
                    getDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert.assertEquals("DeviceConfigRegistryException has wrong message",
                    "Device config for '" + DEVICE_CONFIG_FILE
                            + "' contains a duplicate entry for device id '"
                            + APPLE_IPHONE_DEVICE_ID + "'", e.getMessage());
        }
    }

    @Test
    public void testConstructionWhenDeviceConfigFileUnmarshallsToWrongType()
            throws Throwable {

        final String expectedDeviceConfigs = "wrong type";
        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        replay();

        try {
            new DeviceConfigRegistry(DEVICE_CONFIG_FILE, getMockXmlBinder(),
                    getDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert.assertEquals(
                    "DeviceConfigRegistryException has wrong message",
                    "Expected resource to marshall to a DeviceConfigs but it did not. "
                            + "Resource: '" + DEVICE_CONFIG_FILE + "'", e
                            .getMessage());
        }
    }

    @Test
    public void testConstructionWhenIdentifiedDeviceConfigContainsWrongAbstractDeviceConfigType()
            throws Throwable {

        final DeviceConfigs expectedDeviceConfigs =
                createDeviceConfigsWhenIdentifiedDeviceConfigContainsWrongAbstractDeviceConfigType();

        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        replay();

        try {
            new DeviceConfigRegistry(DEVICE_CONFIG_FILE, getMockXmlBinder(),
                    getDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert.assertEquals(
                    "DeviceConfigRegistryException has wrong message",
                    "IdentifiedDeviceConfig with deviceId '"
                            + APPLE_IPHONE_DEVICE_ID
                            + "' has deviceConfig of wrong type.", e
                            .getMessage());
        }
    }

    @Test
    public void testConstructionWhenUserAgentDeviceConfigContainsWrongAbstractDeviceConfigType()
            throws Throwable {

        final DeviceConfigs expectedDeviceConfigs =
                createDeviceConfigsWhenUserAgentDeviceConfigContainsWrongAbstractDeviceConfigType();

        setupForObjectUnderTestConstruction(expectedDeviceConfigs);

        replay();

        try {
            new DeviceConfigRegistry(DEVICE_CONFIG_FILE, getMockXmlBinder(),
                    getDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert
                    .assertEquals(
                            "DeviceConfigRegistryException has wrong message",
                            "IdentifiedDeviceConfig with deviceId '"
                                    + APPLE_IPHONE_DEVICE_ID
                                    + "' has deviceConfig of wrong type for UserAgentDeviceConfig "
                                    + "with regex of '.*Version/1\\.*'. Expected "
                                    + DeviceConfig.class.getName()
                                    + "; but was "
                                    + getMockDeviceConfig().getClass()
                                            .getName(), e.getMessage());
        }
    }

    private DeviceConfigs
        createDeviceConfigsWhenIdentifiedDeviceConfigContainsWrongAbstractDeviceConfigType() {
        final DeviceConfigs deviceConfigs = new DeviceConfigs();

        IdentifiedDeviceConfig identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID2);
        identifiedDeviceConfig.setDeviceConfig(getDeviceConfig1());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(APPLE_IPHONE_DEVICE_ID);
        identifiedDeviceConfig.setDeviceConfig(getMockDeviceConfig());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        return deviceConfigs;
    }

    private DeviceConfigs
        createDeviceConfigsWhenUserAgentDeviceConfigContainsWrongAbstractDeviceConfigType() {
        final DeviceConfigs deviceConfigs
            = createValidDeviceConfigsContainingOnlyLeafNodeDeviceIds();

        UserAgentDeviceConfig userAgentDeviceConfig = new UserAgentDeviceConfig();
        userAgentDeviceConfig.setUseragentRegex(".*Version/2\\.*");
        userAgentDeviceConfig.setDeviceConfig(getDeviceConfig1());
        deviceConfigs.getIdentifiedDeviceConfig().get(0).getUserAgentDeviceConfig()
            .add(userAgentDeviceConfig);

        userAgentDeviceConfig = new UserAgentDeviceConfig();
        userAgentDeviceConfig.setUseragentRegex(".*Version/1\\.*");
        userAgentDeviceConfig.setDeviceConfig(getMockDeviceConfig());
        deviceConfigs.getIdentifiedDeviceConfig().get(1).getUserAgentDeviceConfig()
            .add(userAgentDeviceConfig);

        return deviceConfigs;
    }

    @Test
    public void testConstructionWhenRuntimeExceptionCaught() throws Throwable {

        final URL deviceConfigUrl =
                TestAbstractDeviceConfigRegistryTestCase.class
                        .getResource(DEVICE_CONFIG_FILE);

        Assert.assertNotNull("Could not load test file from classpath: "
                + DEVICE_CONFIG_FILE, deviceConfigUrl);

        final String expectedDeviceConfigString =
                IOUtils.toString(TestAbstractDeviceConfigRegistryTestCase.class
                        .getResourceAsStream(DEVICE_CONFIG_FILE));

        EasyMock.expect(
                getMockXmlBinder().unmarshall(expectedDeviceConfigString))
                .andThrow(new RuntimeException("test"));

        replay();

        try {
            new DeviceConfigRegistry(DEVICE_CONFIG_FILE, getMockXmlBinder(),
                    getDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert.assertEquals(
                    "DeviceConfigRegistryException has wrong message",
                    "Could not load resource from classpath: '"
                            + DEVICE_CONFIG_FILE + "'", e
                            .getMessage());
        }
    }

    @Test
    public void testConstructionWhenDeviceConfigFileNotFoundOnClasspath() throws Throwable {

        replay();

        try {
            new DeviceConfigRegistry("you cannot find this file", getMockXmlBinder(),
                    getDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert.assertEquals(
                    "DeviceConfigRegistryException has wrong message",
                    "Could not find resource on classpath: 'you cannot find this file'"
                    , e.getMessage());
        }
    }

    /**
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private void setupForObjectUnderTestConstruction(
            final Object expecteDeviceConfigs)
            throws URISyntaxException, IOException {
        final URL deviceConfigUrl =
                TestAbstractDeviceConfigRegistryTestCase.class
                        .getResource(DEVICE_CONFIG_FILE);

        Assert.assertNotNull("Could not load test file from classpath: "
                + DEVICE_CONFIG_FILE, deviceConfigUrl);

        final String expectedDeviceConfigString =
                IOUtils.toString(TestAbstractDeviceConfigRegistryTestCase.class
                        .getResourceAsStream(DEVICE_CONFIG_FILE));

        EasyMock.expect(getMockXmlBinder().unmarshall(expectedDeviceConfigString))
                .andReturn(expecteDeviceConfigs);
    }

    private DeviceConfigs createValidDeviceConfigsContainingOnlyLeafNodeDeviceIds() {
        final DeviceConfigs deviceConfigs = new DeviceConfigs();

        IdentifiedDeviceConfig identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID2);
        identifiedDeviceConfig.setDeviceConfig(getDeviceConfig1());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(APPLE_IPHONE_DEVICE_ID);
        identifiedDeviceConfig.setDeviceConfig(getAppleIphoneDeviceConfig());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        deviceConfigs.setDefaultDeviceConfig(getDefaultDeviceConfig());

        return deviceConfigs;
    }

    private DeviceConfigs
        createValidDeviceConfigsContainingIPhoneOSVersion1Or2UserAgentConfig() {
        final DeviceConfigs deviceConfigs
            = createValidDeviceConfigsContainingOnlyLeafNodeDeviceIds();

        deviceConfigs.getIdentifiedDeviceConfig().get(1).getUserAgentDeviceConfig()
            .add(createUserAgentDeviceConfigMatchIphoneOSVersion1Or2());

        return deviceConfigs;
    }

    private UserAgentDeviceConfig createUserAgentDeviceConfigMatchIphoneOSVersion1Or2() {
        final UserAgentDeviceConfig userAgentDeviceConfig = new UserAgentDeviceConfig();
        userAgentDeviceConfig.setUseragentRegex(".*Version/[12]\\..*");
        userAgentDeviceConfig.setDeviceConfig(getUserAgentDeviceConfigIphoneOSVersion1Or2());
        return userAgentDeviceConfig;
    }

    private DeviceConfigs createValidDeviceConfigsContainingParentDeviceId() {
        final DeviceConfigs deviceConfigs = new DeviceConfigs();

        IdentifiedDeviceConfig identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(APPLE_IPHONE_PARENT_DEVICE_ID);
        identifiedDeviceConfig.setDeviceConfig(getDeviceConfig1());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID2);
        identifiedDeviceConfig.setDeviceConfig(getAppleIphoneDeviceConfig());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        return deviceConfigs;
    }

    private DeviceConfigs createDeviceConfigsContainingDuplicateDeviceId() {
        final DeviceConfigs deviceConfigs = new DeviceConfigs();

        IdentifiedDeviceConfig identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(APPLE_IPHONE_DEVICE_ID);
        identifiedDeviceConfig.setDeviceConfig(getDeviceConfig1());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        identifiedDeviceConfig = new IdentifiedDeviceConfig();
        identifiedDeviceConfig.setDeviceId(APPLE_IPHONE_DEVICE_ID);
        identifiedDeviceConfig.setDeviceConfig(getAppleIphoneDeviceConfig());
        deviceConfigs.getIdentifiedDeviceConfig().add(identifiedDeviceConfig);

        return deviceConfigs;
    }

    /**
     * {@link AbstractDeviceConfigRegistry} extension for testing.
     */
    public static class DeviceConfigRegistry
        extends AbstractDeviceConfigRegistry {

        public DeviceConfigRegistry(final String deviceConfigClasspath,
                final XMLBinder xmlBinder, final DeviceConfig defaultDeviceConfig) {
            super(deviceConfigClasspath, xmlBinder);
        }

        @Override
        protected Class<? extends AbstractDeviceConfig> getDeviceConfigType() {
            return DeviceConfig.class;
        }

    }

    /**
     * {@link DeviceConfigType} extension for testing.
     */
    public static class DeviceConfig extends AbstractDeviceConfig {

    }

    /**
     * {@link DeviceConfigType} extension for testing.
     */
    public static class DeviceConfigWrongType extends AbstractDeviceConfig {

    }

    /**
     * @return the mockXmlBinder
     */
    public XMLBinder getMockXmlBinder() {
        return mockXmlBinder;
    }

    /**
     * @param mockXmlBinder the mockXmlBinder to set
     */
    public void setMockXmlBinder(final XMLBinder mockXmlBinder) {
        this.mockXmlBinder = mockXmlBinder;
    }

    /**
     * @return the objectUnderTest
     */
    public DeviceConfigRegistry getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    public void setObjectUnderTest(final DeviceConfigRegistry objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the deviceConfigXmlFile
     */
    public File getDeviceConfigXmlFile() {
        return deviceConfigXmlFile;
    }

    /**
     * @param deviceConfigXmlFile the deviceConfigXmlFile to set
     */
    public void setDeviceConfigXmlFile(final File deviceConfigXmlFile) {
        this.deviceConfigXmlFile = deviceConfigXmlFile;
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
     * @return the defaultDeviceConfig
     */
    public DeviceConfig getDefaultDeviceConfig() {
        return defaultDeviceConfig;
    }

    /**
     * @param defaultDeviceConfig the defaultDeviceConfig to set
     */
    public void setDefaultDeviceConfig(final DeviceConfig defaultDeviceConfig) {
        this.defaultDeviceConfig = defaultDeviceConfig;
    }

    /**
     * @return the deviceConfig1
     */
    public DeviceConfig getDeviceConfig1() {
        return deviceConfig1;
    }

    /**
     * @param deviceConfig1 the deviceConfig1 to set
     */
    public void setDeviceConfig1(final DeviceConfig deviceConfig1) {
        this.deviceConfig1 = deviceConfig1;
    }

    /**
     * @return the appleIphoneDeviceConfig
     */
    public DeviceConfig getAppleIphoneDeviceConfig() {
        return appleIphoneDeviceConfig;
    }

    /**
     * @param appleIphoneDeviceConfig the appleIphoneDeviceConfig to set
     */
    public void setAppleIphoneDeviceConfig(final DeviceConfig appleIphoneDeviceConfig) {
        this.appleIphoneDeviceConfig = appleIphoneDeviceConfig;
    }

    /**
     * @return the mockDeviceConfig
     */
    public AbstractDeviceConfig getMockDeviceConfig() {
        return mockDeviceConfig;
    }

    /**
     * @param mockDeviceConfig the mockDeviceConfig to set
     */
    public void setMockDeviceConfig(final AbstractDeviceConfig mockDeviceConfig) {
        this.mockDeviceConfig = mockDeviceConfig;
    }

    /**
     * @return the userAgentDeviceConfigIphoneOSVersion1Or2
     */
    public DeviceConfig getUserAgentDeviceConfigIphoneOSVersion1Or2() {
        return userAgentDeviceConfigIphoneOSVersion1Or2;
    }

    /**
     * @param userAgentDeviceConfigIphoneOSVersion1Or2 the userAgentDeviceConfigIphoneOSVersion1Or2 to set
     */
    public void setUserAgentDeviceConfigIphoneOSVersion1Or2(
            final DeviceConfig userAgentDeviceConfigIphoneOSVersion1Or2) {
        this.userAgentDeviceConfigIphoneOSVersion1Or2 =
                userAgentDeviceConfigIphoneOSVersion1Or2;
    }
}
