package au.com.sensis.mobile.web.component.core.sdpcommon.volantis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import au.com.sensis.mobile.web.component.core.sdpcommon.web.Device;

import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;

/**
 * Provides the adaption of a Volantis device to that required by the general mobiles web
 * applications.
 *
 * This object is only valid during the processing of a single request.
 *
 * @author Boyd Sharrock
 */
public class VolantisDeviceAdaptor
        implements Device {

    private static final Logger LOGGER = Logger.getLogger(VolantisDeviceAdaptor.class);

    /** device property name. */
    protected static final String PIXELS_X = "pixelsx";
    /** device property name. */
    protected static final String PIXELS_Y = "pixelsy";
    /** device property name. */
    protected static final String CALL_FORWARD_SUPPORT = "custom.callfwdsup";
    /** device property name. */
    protected static final String JAVASCRIPT_VERSION = "jscriptversion";
    /** device property name. */
    protected static final String POSITIONING_TYPE = "postype";
    /** device property name. */
    protected static final String COOKIE_SUPPORT = "cookies";
    /** device property name. */
    static final String SKIP_META_REFRESH = "custom.skipMetaRefresh";
    /** device property name. */
    static final String DIAL_LINK_INFO = "dial.link.info";
    /** device property name. */
    static final String WTAI_LIBRARIES = "UAProf.WtaiLibraries";
    /** device property value option for  DIAL_LINK_INFO. */
    private static final String DIAL_LINK_INFO_TEL = "tel:";
    /** device property value option for  WTAI_LIBRARIES. */
    private static final String WTA_PUBLIC = "WTA.Public";
    /** device property value option for  WTAI_LIBRARIES. */
    private static final String WTA_PUBLIC_MAKE_CALL = "WTA.Public.makeCall";
    /** device property value option for  custom web 2.0 support. */
    protected static final String WEB_2_SUPPORT = "custom.web2.0";
    /** device property value option for CcppAccept. */
    public static final String CCPP_ACCEPT = "UAProf.CcppAccept";
    /** device property name for getting image size type (L/M/S). */
    public static final String IMAGE_CAGETORY = "custom.imageCategory";
    /** device property value option for IMAGE_CAGETORY. */
    private static final String IMAGE_CAGETORY_LARGE = "L";
    /** device property name. */
    static final String CHARACTERS_X = "charactersx";

    private static final Pattern VCARD_REGEXP = Pattern.compile(".*\\btext/x-vcard\\b.*",
            Pattern.CASE_INSENSITIVE);

    // DK: Having trouble finding any documentation on this Volantis class except
    // for this 3rd party javadoc site:
    // http://www.koders.com/java/fid1D04F4D32479C9F06A2C3FD37659F42B8B0C6A41.aspx
    //              ?s=idef%3Aconfiguration
    private final com.volantis.mcs.devices.Device volantisDevice;

    /**
     * Constructor.
     *
     * @param volantisDevice    the com.volantis.mcs.devices.Device to adapt.
     */
    public VolantisDeviceAdaptor(final com.volantis.mcs.devices.Device volantisDevice) {

        super();

        this.volantisDevice = volantisDevice;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {

        return getVolantisDevice().getName();
    }

    /**
     * {@inheritDoc}
     */
    public int getCharactersX() {

        final String charsXProp = getDevicePropertyAsString(getVolantisDevice(), CHARACTERS_X);

        if (charsXProp == null) {

            throw new RuntimeException("***** >>getCharactersX FAILED *****");
        }

        final int charsX = Integer.valueOf(charsXProp.replaceAll("([^0-9\\-])", ""));

        return charsX;
    }

    /**
     * {@inheritDoc}
     */
    public int getPixelsX() {

        final Integer pixelsX = getDevicePropertyAsInteger(getVolantisDevice(), PIXELS_X);

        if (pixelsX == null) {

            throw new RuntimeException("***** >>getPixelsX FAILED *****");
        }

        return pixelsX;
    }

    /**
     * {@inheritDoc}
     */
    public int getPixelsY() {

        final Integer pixelsY = getDevicePropertyAsInteger(getVolantisDevice(), PIXELS_Y);

        if (pixelsY == null) {

            throw new RuntimeException("***** >>getPixelsY FAILED *****");
        }

        return pixelsY;
    }

    /**
     * {@inheritDoc}
     */
    public String getCallForward() {

        final String callForward = getDevicePropertyAsString(getVolantisDevice(),
                CALL_FORWARD_SUPPORT);

//        if (LOGGER.isDebugEnabled()) {
//
//            LOGGER.debug("custom.callfwdsup = " + callForward);
//        }

        if (callForward == null) {

            throw new RuntimeException("***** >>getCallForward FAILED *****");
        }

       return callForward;
    }

    /**
     * {@inheritDoc}
     */
    public String getJavaScriptVersion() {

        return getDevicePropertyAsString(getVolantisDevice(), JAVASCRIPT_VERSION);
    }

    /**
     * {@inheritDoc}
     */
    public String getPositioningType() {

        return getDevicePropertyAsString(getVolantisDevice(), POSITIONING_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCookiesEnabled() {

        return getDevicePropertyAsBoolean(getVolantisDevice(), COOKIE_SUPPORT);
    }

    /**
     * @return  whether save to contacts is supported.
     */
    public boolean isSaveToContactsSupported() {

        // Adapted from Whereis's jsp:
        // <when expr="tokenize(device:getPolicyValue('UAProf.CcppAccept'), ' ') = 'text/x-vCard' ">

        final String deviceProperty = getDevicePropertyAsString(getVolantisDevice(), CCPP_ACCEPT);
        return (deviceProperty != null) && VCARD_REGEXP.matcher(deviceProperty).matches();
    }


    /**
     * {@inheritDoc}
     */
    public boolean isMetaRefreshSupported() {

        // Adapted from TP's jsp:
        // <sel:when expr="device:getPolicyValue('custom.skipMetaRefresh')='NO'">

        return "NO".equals(getDevicePropertyAsString(getVolantisDevice(), SKIP_META_REFRESH));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTelSupported() {

        // Adapted from TP's jsp:
        // <sel:when expr= "exists(index-of(device:getPolicyValue('dial.link.info'),'tel:'))">

        // NOTE (DK): DIAL_LINK_INFO seems to either be blank, 'tel:' or 'wtai://wp/mc;'
        // However, it seems possible for it to be 'wtai://wp/mc;' but not have an appropriate wtai
        // library (i.e. isWtaiSupported() will return false).
        // e.g. for user-agent = LGE-CX5450 UP.Browser/6.2.2.3.d.1.103 (GUI) MMP/2.0
        // Also note that if both 'tel' and 'wtai' are supported, we seem to prefer 'wtai'.

        return DIAL_LINK_INFO_TEL.equals(
                getDevicePropertyAsString(getVolantisDevice(), DIAL_LINK_INFO));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWtaiSupported() {

        // Adapted from TP's jsp:
        // <sel:when expr="exists(index-of(
        //      device:getPolicyValue('UAProf.WtaiLibraries'),'WTA.Public.makeCall'))
        //   or exists(index-of(device:getPolicyValue('UAProf.WtaiLibraries'),'WTA.Public'))">

        final String[] wtaiLibraries = getDeviceWtaiLibrariesProperty();
        for (final String wtaiLibrary : wtaiLibraries) {

            if (WTA_PUBLIC_MAKE_CALL.equals(wtaiLibrary) || WTA_PUBLIC.equals(wtaiLibrary)) {

                return true;
            }
        }

        return false;
    }

    private String[] getDeviceWtaiLibrariesProperty() {

        final String libraries = getDevicePropertyAsString(getVolantisDevice(), WTAI_LIBRARIES);
        return libraries == null ? new String[0] : libraries.split(" ");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isClickToCallSupported() {

//        if (LOGGER.isDebugEnabled()) {
//
//            LOGGER.debug("Name:" + getName());
//            LOGGER.debug("SKIP_META_REFRESH: "
//                + getDevicePropertyAsString(getVolantisDevice(), SKIP_META_REFRESH));
//            LOGGER.debug("DIAL_LINK_INFO: "
//                    + getDevicePropertyAsString(getVolantisDevice(), DIAL_LINK_INFO));
//            LOGGER.debug("WTAI_LIBRARIES: "
//                    + getDevicePropertyAsString(getVolantisDevice(), WTAI_LIBRARIES));
//            LOGGER.debug("SAVE_TO_CONTACTS_SUPPORT: "
//                    + getDevicePropertyAsString(getVolantisDevice(), CCPP_ACCEPT));
//        }

        return isTelSupported() || isWtaiSupported();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isWeb2Supported() {

        return getDevicePropertyAsBoolean(getVolantisDevice(), WEB_2_SUPPORT);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLargeDevice() {

        return IMAGE_CAGETORY_LARGE.equals(
                getDevicePropertyAsString(getVolantisDevice(), IMAGE_CAGETORY));
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated  not supported anymore.
     */
    @Deprecated
    public int getResultsPerPage() {

        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated  not supported anymore.
     */
    @Deprecated
    public String getUserAgent() {

        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder(this.getClass().getName());

        builder.append(" [ name - " + getName() + " ]");
        builder.append(" [ pixelsX - " + getPixelsX() + " ]");
        builder.append(" [ pixelsY - " + getPixelsY() + " ]");
        builder.append(" [ custom.callfwdsup - " + getCallForward() + " ]");
        builder.append(" [ jscriptversion - " + getJavaScriptVersion() + " ]");
        builder.append(" [ postype - " + getPositioningType() + " ]");
        builder.append(" [ cookies - " + isCookiesEnabled() + " ]");
        builder.append(" [ meta-refreash - " + isMetaRefreshSupported() + " ]");
        builder.append(" [ wtai - " + isWtaiSupported() + " ]");
        builder.append(" [ tel - " + isTelSupported() + " ]");
        builder.append(" [ custom.web2.0 - " + isWeb2Supported() + " ]");

        return builder.toString();
    }

    /**
     * Gets the value for the given property as an {@link Integer}.
     *
     * If the value can't be successfully determined, then null is returned.
     *
     * @param device        the {@link com.volantis.mcs.devices.Device} to get the value from.
     * @param propertyName  the name of the property to retrieve.
     *
     * @return  an {@link Integer} or null.
     */
    private Integer getDevicePropertyAsInteger(final com.volantis.mcs.devices.Device device,
            final String propertyName) {

        final ImmutableMetaDataValue metaDataValue = device.getPolicyMetaDataValue(propertyName);

        if (metaDataValue != null) {

            try {

                /* *
                 * Workaround for bug: http://www.volantis.com/forums/forums/2/topics/215
                 * */
                String prpty = metaDataValue.getAsString();
                final Pattern patrn = Pattern.compile("[a-zA-Z]*([0-9]+)");
                final Matcher mtchr = patrn.matcher(prpty);

                if (mtchr.matches()) {

                    prpty = mtchr.group(1);
                }

                return Integer.valueOf(prpty);

            } catch (final NumberFormatException e) {

                // We can throw this away as its not terminal - it just means there's a wrinkle
                // in the device repository. So we should just fall through and return null.
                LOGGER.warn(">>getDevicePropertyAsInteger FAILED " + "[" + device.getName()
                        + " - " + propertyName + "]", e);
            }
        }

        return null;
    }

    /**
     * Gets the value for the given property as a {@link String}.
     *
     * If the value can't be successfully determined, then null is returned.
     *
     * @param device        the {@link com.volantis.mcs.devices.Device} to get the value from.
     * @param propertyName  the name of the property to retrieve.
     *
     * @return  a {@link String} or null.
     */
    private String getDevicePropertyAsString(final com.volantis.mcs.devices.Device device,
            final String propertyName) {

        final ImmutableMetaDataValue metaDataValue = device.getPolicyMetaDataValue(propertyName);
        if (metaDataValue != null) {

            return metaDataValue.getAsString();
        }

        return null;
    }

    /**
     * Gets the value for the given property as a boolean.
     *
     * If the value can't be successfully determined, then null is returned.
     *
     * @param device        the {@link com.volantis.mcs.devices.Device} to get the value from.
     * @param propertyName  the name of the property to retrieve.
     *
     * @return  false if the property cannot be resolved.
     */
    private boolean getDevicePropertyAsBoolean(final com.volantis.mcs.devices.Device device,
            final String propertyName) {

        boolean propertyBoolean = false;

        final ImmutableMetaDataValue metaDataValue = device.getPolicyMetaDataValue(propertyName);
        if (metaDataValue != null) {

            propertyBoolean = Boolean.valueOf(metaDataValue.getAsString());
        }

        return propertyBoolean;
    }

    /**
     * @return  the volantisDevice.
     */
    protected com.volantis.mcs.devices.Device getVolantisDevice() {

        return volantisDevice;
    }
}
