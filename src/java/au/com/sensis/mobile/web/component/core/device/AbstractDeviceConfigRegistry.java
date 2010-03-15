package au.com.sensis.mobile.web.component.core.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import au.com.sensis.mobile.web.component.core.device.generated.DeviceConfigType;
import au.com.sensis.mobile.web.component.core.device.generated.DeviceConfigsType;
import au.com.sensis.wireless.common.utils.jaxb.XMLBinder;
import au.com.sensis.wireless.common.volantis.devicerepository.api.Device;

/**
 * Base class for a registry that stores device configuration. Loads the device
 * configuration from an XML file and provides the
 * {@link #getDeviceConfig(Device)} method to return a device's configuration.
 * {@link #getDefaultDeviceConfig()} is used if a device's configuration cannot
 * be found.
 *
 * @author Adrian.Koh2@sensis.com.au
 *
 * @param <D>
 *            {@link DeviceConfigType} extension for the specific configuration
 *            data.
 */
public abstract class AbstractDeviceConfigRegistry<D extends DeviceConfigType>
    implements DeviceConfigRegistry<D> {

    private static Logger logger =
            Logger.getLogger(AbstractDeviceConfigRegistry.class);

    private final XMLBinder xmlBinder;

    private final String deviceConfigClasspath;

    private final D defaultDeviceConfig;

    private Map<String, D> deviceConfigMap;

    /**
     * Default constructor.
     *
     * @param deviceConfigClasspath
     *            Classpath reference to the device configuration resource.
     * @param xmlBinder
     *            {@link XMLBinder} to use to parse the configuration.
     * @param defaultDeviceConfig
     *            Default {@link DeviceConfigType} that
     *            {@link #getDeviceConfig(Device)} will return if no specific
     *            configuration is found.
     */
    public AbstractDeviceConfigRegistry(final String deviceConfigClasspath,
            final XMLBinder xmlBinder, final D defaultDeviceConfig) {
        super();
        this.xmlBinder = xmlBinder;
        this.deviceConfigClasspath = deviceConfigClasspath;
        this.defaultDeviceConfig = defaultDeviceConfig;

        loadAndCacheDeviceConfigXmlFile();
    }

    private void loadAndCacheDeviceConfigXmlFile() {
        InputStream deviceConfigInputStream = null;
        try {
            deviceConfigInputStream = getNonNullDeviceConfigInputStreamFromClasspath();
            final String xmlFile = IOUtils.toString(deviceConfigInputStream);

            final DeviceConfigsType deviceConfigsType =
                    (DeviceConfigsType) getXmlBinder().unmarshall(xmlFile);

            initDeviceConfigMap(deviceConfigsType);
        } catch (final ClassCastException e) {
            throw new DeviceConfigRegistryException(
                    "Expected resource to marshall to a DeviceConfigsType but it did not. "
                            + "Resource: '" + getDeviceConfigClasspath() + "'",
                    e);
        } catch (final DeviceConfigRegistryException e) {
            // Prevent our own exceptions from being caught by the catchall below.
            throw e;
        } catch (final Exception e) {
            throw new DeviceConfigRegistryException(
                    "Could not load resource from classpath: '"
                            + getDeviceConfigClasspath() + "'", e);
        } finally {
            if (deviceConfigInputStream != null) {
                closeInputStream(deviceConfigInputStream);
            }
        }

    }

    /**
     * @return
     */
    private InputStream getNonNullDeviceConfigInputStreamFromClasspath() {
        final InputStream deviceConfigInputStream =
                getClass().getResourceAsStream(getDeviceConfigClasspath());
        if (deviceConfigInputStream == null) {
            throw new DeviceConfigRegistryException(
                    "Could not find resource on classpath: '"
                            + getDeviceConfigClasspath() + "'");
        }
        return deviceConfigInputStream;
    }

    private void initDeviceConfigMap(final DeviceConfigsType deviceConfigsType) {
        setDeviceConfigMap(new HashMap<String, D>());

        for (final DeviceConfigType deviceConfig : deviceConfigsType
                .getDeviceConfig()) {
            if (getDeviceConfigMap().get(deviceConfig.getDeviceId()) != null) {
                throw new DeviceConfigRegistryException("Device config for '"
                        + getDeviceConfigClasspath()
                        + "' contains a duplicate entry for device id '"
                        + deviceConfig.getDeviceId() + "'");
            }
            try {
                getDeviceConfigMap().put(deviceConfig.getDeviceId(),
                        (D) deviceConfig);
            } catch (final ClassCastException e) {
                throw new DeviceConfigRegistryException(
                        "Loaded DeviceConfig is of the wrong type: "
                                + deviceConfig.getClass(), e);
            }
        }
    }

    /**
     * @param inputStream
     */
    private void closeInputStream(final InputStream inputStream) {
        try {
            inputStream.close();
        } catch (final IOException e) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn("Could not close input stream for resource: "
                        + getDeviceConfigClasspath(), e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final D getDeviceConfig(final Device device) {
        final Iterator<String> deviceNameHierarchyIterator =
                device.getDeviceNameHierarchyIterator();
        while (deviceNameHierarchyIterator.hasNext()) {
            final String currentDeviceName = deviceNameHierarchyIterator.next();
            final D deviceConfig = getDeviceConfigMap().get(currentDeviceName);
            if (deviceConfig != null) {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug("Returning config for device: '"
                                    + device.getName()
                                    + "'. Actual config name matched (possibly a parent device): '"
                                    + currentDeviceName + "'");
                }
                return deviceConfig;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("No config found for device or its parents: '" + device.getName()
                    + "'");
        }
        return getDefaultDeviceConfig();
    }

    private D getDefaultDeviceConfig() {
        return this.defaultDeviceConfig;
    }

    /**
     * @return the xmlBinder
     */
    private XMLBinder getXmlBinder() {
        return xmlBinder;
    }

    /**
     * @return the deviceConfigMap
     */
    private Map<String, D> getDeviceConfigMap() {
        return deviceConfigMap;
    }

    /**
     * @param deviceConfigMap
     *            the deviceConfigMap to set
     */
    private void setDeviceConfigMap(final Map<String, D> deviceConfigMap) {
        this.deviceConfigMap = deviceConfigMap;
    }

    /**
     * @return the deviceConfigClasspath
     */
    private String getDeviceConfigClasspath() {
        return deviceConfigClasspath;
    }

}
