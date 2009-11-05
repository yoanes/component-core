package au.com.sensis.mobile.web.component.core.sdpcommon.web;

/**
 * Represents the computing device that the user is using to browse the site.
 *
 * @author Boyd Sharrock.
 */
public interface Device {

    /**
     * @return  the human friendly name for this device.
     */
    String getName();

    /**
     * @return  the maximum number of characters that fit on screen horisontaly before scrolling.
     */
    int getCharactersX();

    /**
     * @return  the usable width in pixels for this device.
     */
    int getPixelsX();

    /**
     * @return  the usable height in pixels for this device.
     */
    int getPixelsY();


    /**
     *  @return the call forward link from this device.
     */
    String getCallForward();

    /**
     * @return  true if this device supports cookies.
     */
    boolean isCookiesEnabled();

    /**
     * @return  the JavaScript version for this device.
     */
    String getJavaScriptVersion();

    /**
     * @return  the positioning type for this device.
     */
    String getPositioningType();

    /**
     * @return  the number of results per page for this device.
     */
    int getResultsPerPage();

    /**
     * @return  the user agent string for this device.
     */
    String getUserAgent();

    /**
     * @return true if this device supports meta-refresh.
     */
    boolean isMetaRefreshSupported();

    /**
     * @return true if this device supports WTAI (ie wtai://wp/mc;nnnnnnnn).
     */
    boolean isWtaiSupported();

    /**
     * @return true if this device supports tel (ie tel:nnnnnnnn).
     */
    boolean isTelSupported();

    /**
     * @return true if the device supports click to call (wtai or tel).
     */
    boolean isClickToCallSupported();

    /**
     * @return true if the device supports our custom Web 2.0 functionality.
     */
    boolean isWeb2Supported();

    /**
     * @return true if the device size category is a large device.
     */
    boolean isLargeDevice();
}
