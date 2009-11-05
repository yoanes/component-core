package au.com.sensis.mobile.web.component.core.sdpcommon.web;

/**
 * Provides the fallback device for when we fail to recognise a device.
 *
 * @author Boyd Sharrock (cloned from TPM)
 */
public class DefaultDevice
        implements Device {

    /** Default name. */
    protected static final String DEFAULT_NAME = "Default Device";

    /** Default pixelsX. */
    protected static final int DEFAULT_CARACTERS_X = 28;

    /** Default pixelsX. */
    protected static final int DEFAULT_PIXELS_X = 165;

    /** Default pixelsY. */
    protected static final int DEFAULT_PIXELS_Y = 230;

    /** Default callForward. */
    protected static final String DEFAULT_CALL_FORWARD = "none";

    /** Default javascript version. */
    protected static final String DEFAULT_JAVASCRIPT_VERSION = "1.2";

    /** Default positioningType. */
    protected static final String DEFAULT_POSITIONING_TYPE = "none";

    /** Default resultsPerPage. */
    protected static final int DEFAULT_RESULTS_PER_PAGE = 0;

    /** Default userAgent. */
    protected static final String DEFAULT_USER_AGENT = "none";

    /** Default cookiesEnabled. */
    protected static final boolean DEFAULT_COOKIES_ENABLED = false;

    /** Default web 2.0. */
    protected static final boolean DEFAULT_WEB2_SUPPORT = false;

    /**
     * {@inheritDoc}
     */
    public String getName() {

        return DEFAULT_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public int getCharactersX() {

        return DEFAULT_CARACTERS_X;
    }

    /**
     * {@inheritDoc}
     */
    public int getPixelsX() {

        return DEFAULT_PIXELS_X;
    }

    /**
     * {@inheritDoc}
     */
    public int getPixelsY() {

        return DEFAULT_PIXELS_Y;
    }

    /**
     * {@inheritDoc}
     */
    public String getCallForward() {

        return DEFAULT_CALL_FORWARD;
    }

    /**
     * {@inheritDoc}
     */
    public String getJavaScriptVersion() {

        return DEFAULT_JAVASCRIPT_VERSION;
    }

    /**
     * {@inheritDoc}
     */
    public String getPositioningType() {

        return DEFAULT_POSITIONING_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    public int getResultsPerPage() {

        return DEFAULT_RESULTS_PER_PAGE;
    }

    /**
     * {@inheritDoc}
     */
    public String getUserAgent() {

        return DEFAULT_USER_AGENT;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCookiesEnabled() {

        return DEFAULT_COOKIES_ENABLED;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMetaRefreshSupported() {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTelSupported() {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWtaiSupported() {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isClickToCallSupported() {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWeb2Supported() {

        return DEFAULT_WEB2_SUPPORT;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isLargeDevice() {

        return false;
    }
}
