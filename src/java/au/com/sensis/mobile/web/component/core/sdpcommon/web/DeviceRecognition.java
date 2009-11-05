package au.com.sensis.mobile.web.component.core.sdpcommon.web;


/**
 * Supports recognition of the device based on the user-agent header.
 *
 * @author Boyd Sharrock (cloned from TPM)
 */
public interface DeviceRecognition {

    /**
     * Identify the device.
     *
     * A Device should always be returned, even when not authoritative.
     *
     * @param userAgent the "user-agent" header value.
     *
     * @return  a {@link Device}.
     */
    Device identifyDevice(String userAgent);
}
