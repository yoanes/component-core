package au.com.sensis.mobile.web.component.core.device;

/**
 * Exception to be thrown by {@link DeviceConfigRegistry} implementations.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class DeviceConfigRegistryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message Message describing this exception.
     */
    public DeviceConfigRegistryException(final String message) {
        super(message);
    }

    /**
     * @param message Message describing this exception.
     * @param cause Cause of this exception.
     */
    public DeviceConfigRegistryException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
