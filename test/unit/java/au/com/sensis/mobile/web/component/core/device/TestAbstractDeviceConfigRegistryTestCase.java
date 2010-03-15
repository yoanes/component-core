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

import au.com.sensis.mobile.web.component.core.device.generated.DeviceConfigType;
import au.com.sensis.mobile.web.component.core.device.generated.DeviceConfigsType;
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

    private static final String LEAF_NODE_DEVICE_ID1 = "device1";
    private static final String PARENT_DEVICE_ID1 = "parentDevice2";
    private static final String LEAF_NODE_DEVICE_ID2 = "device2";
    private static final String DEVICE_ID_NOT_FOUND = "notFoundId";
    private static final String DEVICE_ID_NOT_FOUND_PARENT = "parentNotFoundId";
    private static final String DEFAULT_DEVICE_ID = "defaultDevice";

    private XMLBinder mockXmlBinder;

    private File deviceConfigXmlFile;

    private DeviceConfigRegistry objectUnderTest;

    private Device mockDevice;

    private DeviceConfig defaultDeviceConfig;

    /**
     * Setup test data.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setDefaultDeviceConfig(createDefaultDeviceConfig());
    }

    private DeviceConfig createDefaultDeviceConfig() {
        final DeviceConfig deviceConfig = new DeviceConfig();
        deviceConfig.setDeviceId(DEFAULT_DEVICE_ID);
        return deviceConfig;
    }

    @Test
    public void testGetDeviceConfigWhenFound() throws Throwable {

        final DeviceConfigsType expectedDeviceConfigsType
            = createValidDeviceConfigsTypeContainingOnlyLeafNodes();
        setupForObjectUnderTestConstruction(expectedDeviceConfigsType);

        EasyMock.expect(getMockDevice().getDeviceNameHierarchyIterator()).andReturn(
                createValidDeviceNameHierarchyIterator()).atLeastOnce();

        // Expectation to cover off potential debug logging.
        EasyMock.expect(getMockDevice().getName()).andReturn(
                LEAF_NODE_DEVICE_ID1).anyTimes();

        replay();

        // Due to the constructor invoking mocks, we must instantiate
        // the object under test in this method instead of in the setup method.
        setObjectUnderTest(new DeviceConfigRegistry(DEVICE_CONFIG_FILE,
                getMockXmlBinder(), createDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig =
                getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertSame("message", expectedDeviceConfigsType.getDeviceConfig().get(1),
                actualDeviceConfig);

    }

    @Test
    public void testGetDeviceConfigWhenParentFound() throws Throwable {

        final DeviceConfigsType expectedDeviceConfigsType
            = createValidDeviceConfigsTypeContainingParentId();
        setupForObjectUnderTestConstruction(expectedDeviceConfigsType);

        EasyMock.expect(getMockDevice().getDeviceNameHierarchyIterator()).andReturn(
                createValidDeviceNameHierarchyIterator()).atLeastOnce();

        // Expectation to cover off potential debug logging.
        EasyMock.expect(getMockDevice().getName()).andReturn(
                LEAF_NODE_DEVICE_ID1).anyTimes();

        replay();

        // Due to the constructor invoking mocks, we must instantiate
        // the object under test in this method instead of in the setup method.
        setObjectUnderTest(new DeviceConfigRegistry(DEVICE_CONFIG_FILE,
                getMockXmlBinder(), createDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig =
            getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertSame("message", expectedDeviceConfigsType.getDeviceConfig().get(0),
                actualDeviceConfig);

    }

    private Iterator<String> createValidDeviceNameHierarchyIterator() {
        return Arrays.asList(LEAF_NODE_DEVICE_ID1, PARENT_DEVICE_ID1).iterator();
    }

    @Test
    public void testGetDeviceConfigWhenNotFound() throws Throwable {

        final DeviceConfigsType expectedDeviceConfigsType
            = createValidDeviceConfigsTypeContainingOnlyLeafNodes();
        setupForObjectUnderTestConstruction(expectedDeviceConfigsType);

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
                getMockXmlBinder(), createDefaultDeviceConfig()));

        final DeviceConfig actualDeviceConfig =
            getObjectUnderTest().getDeviceConfig(getMockDevice());
        Assert.assertEquals("message", createDefaultDeviceConfig().getDeviceId(),
                actualDeviceConfig.getDeviceId());

    }

    @Test
    public void testConstructionWhenDeviceConfigContainsDuplicateDeviceId()
            throws Throwable {
        final DeviceConfigsType expectedDeviceConfigsType =
                createDeviceConfigsTypeContainingDuplicateId();
        setupForObjectUnderTestConstruction(expectedDeviceConfigsType);

        replay();

        try {
            new DeviceConfigRegistry(DEVICE_CONFIG_FILE, getMockXmlBinder(),
                    createDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert.assertEquals("DeviceConfigRegistryException has wrong message",
                    "Device config for '" + DEVICE_CONFIG_FILE
                            + "' contains a duplicate entry for device id '"
                            + LEAF_NODE_DEVICE_ID1 + "'", e.getMessage());
        }
    }

    @Test
    public void testConstructionWhenDeviceConfigFileUnmarshallsToWrongType()
            throws Throwable {

        final String expectedDeviceConfigsType = "wrong type";
        setupForObjectUnderTestConstruction(expectedDeviceConfigsType);

        replay();

        try {
            new DeviceConfigRegistry(DEVICE_CONFIG_FILE, getMockXmlBinder(),
                    createDefaultDeviceConfig());
            Assert.fail("DeviceConfigRegistryException expected");
        } catch (final DeviceConfigRegistryException e) {
            Assert.assertEquals(
                    "DeviceConfigRegistryException has wrong message",
                    "Expected resource to marshall to a DeviceConfigsType but it did not. "
                            + "Resource: '" + DEVICE_CONFIG_FILE + "'", e
                            .getMessage());
        }
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
                    createDefaultDeviceConfig());
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
                    createDefaultDeviceConfig());
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
            final Object expecteDeviceConfigsType)
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
                .andReturn(expecteDeviceConfigsType);
    }

    private DeviceConfigsType createValidDeviceConfigsTypeContainingOnlyLeafNodes() {
        final DeviceConfigsType deviceConfigsType = new DeviceConfigsType();

        DeviceConfig deviceConfig = new DeviceConfig();
        deviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID2);
        deviceConfigsType.getDeviceConfig().add(deviceConfig);

        deviceConfig = new DeviceConfig();
        deviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID1);
        deviceConfigsType.getDeviceConfig().add(deviceConfig);

        return deviceConfigsType;
    }

    private DeviceConfigsType createValidDeviceConfigsTypeContainingParentId() {
        final DeviceConfigsType deviceConfigsType = new DeviceConfigsType();

        DeviceConfig deviceConfig = new DeviceConfig();
        deviceConfig.setDeviceId(PARENT_DEVICE_ID1);
        deviceConfigsType.getDeviceConfig().add(deviceConfig);

        deviceConfig = new DeviceConfig();
        deviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID2);
        deviceConfigsType.getDeviceConfig().add(deviceConfig);

        return deviceConfigsType;
    }

    private DeviceConfigsType createDeviceConfigsTypeContainingDuplicateId() {
        final DeviceConfigsType deviceConfigsType = new DeviceConfigsType();

        DeviceConfig deviceConfig = new DeviceConfig();
        deviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID1);
        deviceConfigsType.getDeviceConfig().add(deviceConfig);

        deviceConfig = new DeviceConfig();
        deviceConfig.setDeviceId(LEAF_NODE_DEVICE_ID1);
        deviceConfigsType.getDeviceConfig().add(deviceConfig);

        return deviceConfigsType;
    }

    /**
     * {@link AbstractDeviceConfigRegistry} extension for testing.
     */
    public static class DeviceConfigRegistry
        extends AbstractDeviceConfigRegistry<DeviceConfig> {

        public DeviceConfigRegistry(final String deviceConfigClasspath,
                final XMLBinder xmlBinder, final DeviceConfig defaultDeviceConfig) {
            super(deviceConfigClasspath, xmlBinder, defaultDeviceConfig);
        }

    }

    /**
     * {@link DeviceConfigType} extension for testing.
     */
    public static class DeviceConfig extends DeviceConfigType {

    }

    /**
     * {@link DeviceConfigType} extension for testing.
     */
    public static class DeviceConfigWrongType extends DeviceConfigType {

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
}
