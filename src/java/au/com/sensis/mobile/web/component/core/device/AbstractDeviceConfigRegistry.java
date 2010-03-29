package au.com.sensis.mobile.web.component.core.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import au.com.sensis.mobile.web.component.core.device.generated.AbstractDeviceConfig;
import au.com.sensis.mobile.web.component.core.device.generated.DeviceConfigs;
import au.com.sensis.mobile.web.component.core.device.generated.IdentifiedDeviceConfig;
import au.com.sensis.mobile.web.component.core.device.generated.UserAgentDeviceConfig;
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
 */
public abstract class AbstractDeviceConfigRegistry
    implements DeviceConfigRegistry {

    private static Logger logger =
            Logger.getLogger(AbstractDeviceConfigRegistry.class);

    private final XMLBinder xmlBinder;

    private final String deviceConfigClasspath;

    private AbstractDeviceConfig defaultDeviceConfig;

    private Map<String, IdentifiedDeviceConfig> deviceConfigMap;

    /**
     * Default constructor.
     *
     * @param deviceConfigClasspath
     *            Classpath reference to the device configuration resource.
     * @param xmlBinder
     *            {@link XMLBinder} to use to parse the configuration.
     */
    public AbstractDeviceConfigRegistry(final String deviceConfigClasspath,
            final XMLBinder xmlBinder) {
        super();
        this.xmlBinder = xmlBinder;
        this.deviceConfigClasspath = deviceConfigClasspath;

        loadAndCacheDeviceConfigXmlFile();
    }

    /**
     * Subclasses should override this to indicate what type of config is
     * expected to be parsed from the {@link #getDeviceConfigClasspath()} file.
     *
     * @return {@link Class} indicating the type of config to be parsed from the
     *         {@link #getDeviceConfigClasspath()}.
     */
    protected abstract Class<? extends AbstractDeviceConfig> getDeviceConfigType();

    private void loadAndCacheDeviceConfigXmlFile() {
        InputStream deviceConfigInputStream = null;
        try {
            deviceConfigInputStream = getNonNullDeviceConfigInputStreamFromClasspath();
            final String xmlFile = IOUtils.toString(deviceConfigInputStream);

            final DeviceConfigs deviceConfigs =
                    (DeviceConfigs) getXmlBinder().unmarshall(xmlFile);

            initDeviceConfigMap(deviceConfigs);
            setDefaultDeviceConfig(deviceConfigs.getDefaultDeviceConfig());
        } catch (final ClassCastException e) {
            throw new DeviceConfigRegistryException(
                    "Expected resource to marshall to a DeviceConfigs but it did not. "
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

    private void initDeviceConfigMap(final DeviceConfigs deviceConfigs) {
        setDeviceConfigMap(new HashMap<String, IdentifiedDeviceConfig>());

        for (final IdentifiedDeviceConfig deviceConfig : deviceConfigs
                .getIdentifiedDeviceConfig()) {
            if (getDeviceConfigMap().get(deviceConfig.getDeviceId()) != null) {
                throw new DeviceConfigRegistryException("Device config for '"
                        + getDeviceConfigClasspath()
                        + "' contains a duplicate entry for device id '"
                        + deviceConfig.getDeviceId() + "'");
            }

            getDeviceConfigMap().put(deviceConfig.getDeviceId(),
                    deviceConfig);
        }

        validateDeviceConfigTypes();
    }

    private void validateDeviceConfigTypes() {
        for (final Entry<String, IdentifiedDeviceConfig> currIdentifiedDeviceConfig
                : getDeviceConfigMap().entrySet()) {

            try {
                getDeviceConfigType().cast(
                        currIdentifiedDeviceConfig.getValue().getDeviceConfig());
            } catch (final ClassCastException e) {
                throw new DeviceConfigRegistryException(
                        "IdentifiedDeviceConfig with deviceId '"
                                + currIdentifiedDeviceConfig.getKey()
                                + "' has deviceConfig of wrong type.");
            }

            validateUserAgentDeviceConfigTypes(currIdentifiedDeviceConfig);
        }

    }

    private void validateUserAgentDeviceConfigTypes(
            final Entry<String, IdentifiedDeviceConfig> currIdentifiedDeviceConfig) {

        for (final UserAgentDeviceConfig userAgentDeviceConfig : currIdentifiedDeviceConfig
                .getValue().getUserAgentDeviceConfig()) {

            try {
                getDeviceConfigType().cast(
                        userAgentDeviceConfig.getDeviceConfig());
            } catch (final ClassCastException e) {
                throw new DeviceConfigRegistryException(
                        "IdentifiedDeviceConfig with deviceId '"
                                + currIdentifiedDeviceConfig.getKey()
                                + "' has deviceConfig of wrong type for UserAgentDeviceConfig "
                                + "with regex of '" + userAgentDeviceConfig.getUseragentRegex()
                                + "'. Expected " + getDeviceConfigType().getName() + "; but was "
                                + userAgentDeviceConfig.getDeviceConfig().getClass().getName());
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
    public final AbstractDeviceConfig getDeviceConfig(final Device device) {
        final Iterator<String> deviceNameHierarchyIterator =
                device.getDeviceNameHierarchyIterator();

        // The iterator we walk through will return a chain like "Apple-iphone", "Apple",
        // ..., "Master", when using Volantis.
        while (deviceNameHierarchyIterator.hasNext()) {
            final String currentDeviceHierarchyNodeName =
                    deviceNameHierarchyIterator.next();
            final IdentifiedDeviceConfig identifiedDeviceConfig =
                    getDeviceConfigMap().get(currentDeviceHierarchyNodeName);
            if (identifiedDeviceConfig != null) {
                return getDeviceConfig(device, currentDeviceHierarchyNodeName,
                        identifiedDeviceConfig);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("No IdentifiedDeviceConfig found for device or its parents: '"
                    + device.getName() + "'");
        }
        return getDefaultDeviceConfig();
    }

    private AbstractDeviceConfig getDeviceConfig(final Device device,
            final String currentDeviceHierarchyNodeName,
            final IdentifiedDeviceConfig identifiedDeviceConfig) {

        // We have already found an IdentifiedDeviceConfig but it
        // may contain extra filtering on the UserAgent. So lets check those.
        for (final UserAgentDeviceConfig userAgentDeviceConfig : identifiedDeviceConfig
                .getUserAgentDeviceConfig()) {
            if (device.getUserAgent().matches(
                    userAgentDeviceConfig.getUseragentRegex())) {

                logDebugUserAgentDeviceConfigFound(device,
                        currentDeviceHierarchyNodeName);

                // Return type is guaranteed to be of type governed by getDeviceConfigType
                // due to constraint enforced by the constructor.
                return userAgentDeviceConfig.getDeviceConfig();
            }
        }

        logDebugIdentifiedDeviceConfigFound(device,
                currentDeviceHierarchyNodeName);

        // No UserAgent specfic configs were found, so lets return the IdentifiedDeviceConfig's
        // DeviceConfig.
        // Return type is guaranteed to be of type governed by getDeviceConfigType
        // due to constraint enforced by the constructor.
        return identifiedDeviceConfig.getDeviceConfig();
    }

    /**
     * @param device
     * @param currentDeviceHierarchyNodeName
     */
    private void logDebugIdentifiedDeviceConfigFound(final Device device,
            final String currentDeviceHierarchyNodeName) {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Returning device from the IdentifiedDeviceConfig found for device: '"
                    + device.getName()
                    + "'. Actual IdentifiedDeviceConfig name matched (possibly a parent device): '"
                    + currentDeviceHierarchyNodeName + "'");
        }
    }

    /**
     * @param device
     * @param currentDeviceHierarchyNodeName
     */
    private void logDebugUserAgentDeviceConfigFound(final Device device,
            final String currentDeviceHierarchyNodeName) {
        if (logger.isDebugEnabled()) {
            logger.debug(
                "Returning device from the UserAgentDeviceConfig found for device: '"
                + device.getName()
                + "'; userAgent: "
                + device.getUserAgent()
                + "'. Actual IdentifiedDeviceConfig name matched (possibly a parent device): '"
                + currentDeviceHierarchyNodeName + "'");
        }
    }

    /**
     * @param defaultDeviceConfig the defaultDeviceConfig to set
     */
    private void setDefaultDeviceConfig(final AbstractDeviceConfig defaultDeviceConfig) {
        this.defaultDeviceConfig = defaultDeviceConfig;
    }

    /**
     * @return Default {@link AbstractDeviceConfig} that
     *         {@link #getDeviceConfig(Device)} will return if no specific
     *         configuration is found.
     */
    private AbstractDeviceConfig getDefaultDeviceConfig() {
        return defaultDeviceConfig;
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
    private Map<String, IdentifiedDeviceConfig> getDeviceConfigMap() {
        return deviceConfigMap;
    }

    /**
     * @param deviceConfigMap
     *            the deviceConfigMap to set
     */
    private void setDeviceConfigMap(final Map<String, IdentifiedDeviceConfig> deviceConfigMap) {
        this.deviceConfigMap = deviceConfigMap;
    }

    /**
     * @return the deviceConfigClasspath
     */
    private String getDeviceConfigClasspath() {
        return deviceConfigClasspath;
    }

}
