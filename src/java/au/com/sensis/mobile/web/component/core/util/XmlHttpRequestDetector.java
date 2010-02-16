package au.com.sensis.mobile.web.component.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Utility class for detecting whether a request is an XmlHttpRequest. This
 * class assumes that it will be instantiated (so
 * {@link #isXmlHttpRequest(HttpServletRequest)} is not static). This allows
 * this class to be mocked during unit testing.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class XmlHttpRequestDetector {

    /**
     * Name of the request header to be checked.
     */
    public static final String X_REQUESTED_WITH_HEADER_NAME =
            "X-Requested-With";

    /**
     * Value of the request header that indicates an XmlHttpRequest.
     */
    public static final String X_REQUESTED_WITH_HEADER_VALUE = "XMLHttpRequest";

    /**
     * Some phones like the Nokia 6120 do not allow request headers to be set so
     * mootools fails to set the 'X-Requested-With header' to 'XMLHttpRequest'
     * (and the browser itself doesn't set this). So we always check for an
     * additional fallback request parameter corresponding to the header (which
     * our custom JavaScript code should set).
     */
    public static final String X_REQUESTED_WITH_PARAM_NAME = "xrw";

    /**
     * Value of the request param that indicates an XmlHttpRequest.
     */
    public static final String X_REQUESTED_WITH_PARAM_VALUE = "xhr";

    /**
     * Returns true if the given {@link HttpServletRequest} is an
     * XMLHttpRequest.
     *
     * @param httpServletRequest
     *            {@link HttpServletRequest} of the current request.
     * @return true if the given {@link HttpServletRequest} is an
     *         XMLHttpRequest.
     */
    public boolean isXmlHttpRequest(final HttpServletRequest httpServletRequest) {
        return isAjaxRequestHeaderSet(httpServletRequest)
                || isAjaxRequestParamSet(httpServletRequest);
    }

    /**
     * @param httpServletRequest
     * @return
     */
    private boolean isAjaxRequestHeaderSet(
            final HttpServletRequest httpServletRequest) {
        return X_REQUESTED_WITH_HEADER_VALUE
                .equalsIgnoreCase(httpServletRequest
                        .getHeader(X_REQUESTED_WITH_HEADER_NAME));
    }

    /**
     * @param httpServletRequest
     * @return
     */
    private boolean isAjaxRequestParamSet(
            final HttpServletRequest httpServletRequest) {
        return X_REQUESTED_WITH_PARAM_VALUE.equalsIgnoreCase(httpServletRequest
                .getParameter(X_REQUESTED_WITH_PARAM_NAME));
    }

}
