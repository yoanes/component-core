package au.com.sensis.mobile.web.component.core.sdpcommon.volantis;

import org.apache.log4j.Logger;

import au.com.sensis.mobile.web.component.core.sdpcommon.web.DefaultDevice;
import au.com.sensis.mobile.web.component.core.sdpcommon.web.DeviceRecognition;
import au.com.sensis.wireless.web.common.exception.ApplicationRuntimeException;
import au.com.sensis.wireless.web.common.validation.Validatable;

import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.http.HttpFactory;
import com.volantis.mcs.http.MutableHttpHeaders;
import com.volantis.mcs.servlet.MarinerServletApplication;

/**
 * Provides DeviceRecognition by using Volantis.
 *
 * This will only work where Volantis MCS is running in the same JVM as this class.
 *
 * @author Boyd Sharrock.
 */
public class VolantisDeviceRecognition
        implements DeviceRecognition, Validatable {

    private static final Logger LOGGER = Logger.getLogger(VolantisDeviceRecognition.class);

    private static final String HTTP_USER_AGENT = "user-agent";

    /** Provides the default device when we fail to recognise the user device. */
    private static final au.com.sensis.mobile.web.component.core.sdpcommon.web.Device DEFAULT_DEVICE
            = new DefaultDevice();

    /**
     * Access the Volantis device repository instance and identify the device.
     *
     * @param userAgent the {@link String} "user-agent" header.
     *
     * @return  a {@link au.com.sensis.mobile.web.component.core.sdpcommon.web.Device}.
     */
    public au.com.sensis.mobile.web.component.core.sdpcommon.web.Device identifyDevice(
            final String userAgent) {

        au.com.sensis.mobile.web.component.core.sdpcommon.web.Device device = null;
        final MutableHttpHeaders headers = newMutableHttpHeaders();
        headers.addHeader(HTTP_USER_AGENT, userAgent);

        try {

            // Identify our Volantis device and adapts to app-specific Device
            final Device volantisDevice = getDeviceRepository().getDevice(headers);
            if (volantisDevice != null) {

                device = newVolantisDeviceAdaptor(volantisDevice);
            }

        } catch (final DeviceRepositoryException e) {

            LOGGER.error("Device Recognition Failed", e);
        }

        // If we still can't identify the device, return a default.
        if (device == null) {

            device = getDefaultDevice();
        }

        return device;
    }

    /**
     * {@inheritDoc}
     */
    public void validateState()
            throws ApplicationRuntimeException {

        // nothing to do.
    }

    /**
     * @return  the Volantis {@link DeviceRepository}.
     *
     * @throws DeviceRepositoryException    if there is a problem accessing the Device Repository.
     */
    protected DeviceRepository getDeviceRepository()
            throws DeviceRepositoryException {

        // Access here is deprecated - need to talk to Volantis about what has replaced it...
        // TODO: I think the method to call is getInstance(ServletContext context).
        // TODO: We need access to the ServletContext to be able to call it.
        return MarinerServletApplication.getInstance().getRuntimeDeviceRepository();
    }

    /**
     * @return  a new {@link MutableHttpHeaders}.
     */
    protected MutableHttpHeaders newMutableHttpHeaders() {

        return HttpFactory.getDefaultInstance().createHTTPHeaders();
    }

    /**
     * @param device    the {@link Device} to get the {@link VolantisDeviceAdaptor} for.
     *
     * @return  a {@link VolantisDeviceAdaptor} for the given {@link Device}.
     */
    protected VolantisDeviceAdaptor newVolantisDeviceAdaptor(final Device device) {

        return new VolantisDeviceAdaptor(device);
    }

    /**
     * @return  the default {@link au.com.sensis.mobile.web.component.core.sdpcommon.web.Device}.
     */
    protected au.com.sensis.mobile.web.component.core.sdpcommon.web.Device getDefaultDevice() {

        return DEFAULT_DEVICE;
    }
}
