package au.com.sensis.mobile.web.component.core.device;

import au.com.sensis.mobile.web.component.core.device.generated.DeviceConfigType;
import au.com.sensis.wireless.common.volantis.devicerepository.api.Device;

/**
 * Simple interface for a registry that maps {@link Device}s to corresponding
 * configuration.
 *
 * @author Adrian.Koh2@sensis.com.au
 *
 * @param <D> Type of the config that this registry handles.
 */
public interface DeviceConfigRegistry<D extends DeviceConfigType> {

    /**
     * Get the configuration for the given device.
     *
     * @param device
     *            Device to get the configuration from.
     * @return configuration for the given device.
     * @throws DeviceConfigRegistryException Thrown if any error occurs.
     */
    D getDeviceConfig(final Device device) throws DeviceConfigRegistryException;

}
