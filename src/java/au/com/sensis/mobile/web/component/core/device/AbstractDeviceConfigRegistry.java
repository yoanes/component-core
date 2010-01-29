package au.com.sensis.mobile.web.component.core.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
            deviceConfigInputStream =
                    getClass().getResourceAsStream(getDeviceConfigClasspath());
            final String xmlFile = IOUtils.toString(deviceConfigInputStream);

            final DeviceConfigsType deviceConfigsType =
                    (DeviceConfigsType) getXmlBinder().unmarshall(xmlFile);

            initDeviceConfigMap(deviceConfigsType);
        } catch (final ClassCastException e) {
            throw new RuntimeException(
                    "Expected resource to marshall to a DeviceConfigsType but it did not. "
                            + "Resource: '" + getDeviceConfigClasspath() + "'",
                    e);
        } catch (final Exception e) {
            throw new RuntimeException(
                    "Could not load resource from classpath: '"
                            + getDeviceConfigClasspath() + "'", e);
        } finally {
            if (deviceConfigInputStream != null) {
                closeInputStream(deviceConfigInputStream);
            }
        }

    }

    private void initDeviceConfigMap(final DeviceConfigsType deviceConfigsType) {
        setDeviceConfigMap(new HashMap<String, D>());

        for (final DeviceConfigType deviceConfig : deviceConfigsType
                .getDeviceConfig()) {
            try {
                getDeviceConfigMap().put(deviceConfig.getDeviceId(),
                        (D) deviceConfig);
            } catch (final ClassCastException e) {
                throw new RuntimeException(
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
        final D deviceConfig = getDeviceConfigMap().get(device.getName());
        if (deviceConfig != null) {
            return deviceConfig;
        } else {
            return getDefaultDeviceConfig();
        }
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
