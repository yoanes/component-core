package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import au.com.sensis.wireless.web.common.exception.ApplicationRuntimeException;
import au.com.sensis.wireless.web.common.validation.Validatable;
import au.com.sensis.wireless.web.common.validation.ValidatableUtils;

/**
 * Maps the {@value #BUNDLE_EXPLODER_REQUEST_PARAM_NAME} request parameter to
 * the
 * {@value ResourceBundleLoaderController#BUNDLE_EXPLODER_REQUEST_SESSION_KEY}
 * session attribute. This enables the following use case for development
 * environments:
 * <ol>
 * <li>User navigates to a page in the app that contains &lt;script ... /&gt;
 * nodes.</li>
 * <li>User edits the URL of the current page and appends the
 * above request parameter, set to {@value #BOOLEAN_TRUE_PARAM_VALUE} (or false).</li>
 * <li>The new request passes through this filter, resulting in session state
 * being set (as long as the corresponding feature in
 * {@link #getFeatureEnablementRegistry()} is enabled).</li>
 * <li>The response is returned to the client device.</li>
 * <li>The client device downloads the URLs contained by the &lt;script ...
 * /&gt; nodes.</li>
 * <li>The script node URLs enter the bundle handling controllers (see
 * {@link AbstractResourceBundleLoaderController} and its subclasses) . These
 * controllers check the session state for the above keys and vary their
 * behaviour accordingly.</li>
 * </ol>
 * Note the following:
 * <ol>
 * <li>Once the above request parameter has been set on a URL, every
 * subsequent page will be affected until the request parameter is manually set
 * to the inverse value.</li>
 * <li>Session state is only set if the corresponding flag in
 * {@link #getFeatureEnablementRegistry()} is set.</li>
 * </ol>
 * <p>
 * This filter is best instantiated via Spring's
 * {@link org.springframework.web.filter.DelegatingFilterProxy}.
 * </p>
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class BundleExploderActivatonFilter implements Filter,
        Validatable {

    private static Logger logger = Logger.getLogger(
            BundleExploderActivatonFilter.class);

    /**
     * Name of the request parameter that requests that the bundle exploder be
     * returned instead of the bundle itself.
     */
    public static final String BUNDLE_EXPLODER_REQUEST_PARAM_NAME = "jsbe";

    /**
     * Request parameter value that is interpreted as "true" by this class. All
     * other values are considered false.
     */
    public static final String BOOLEAN_TRUE_PARAM_VALUE = "1";

    private FeatureEnablementRegistry featureEnablementRegistry;

    /**
     * Initial value corresponding to {@link #BUNDLE_EXPLODER_REQUEST_PARAM_NAME}.
     * Only used if {@link FeatureEnablementRegistry#isBundleExplosionEnabled()} is true.
     * Defaults to false.
     */
    private boolean bundleExplosionInitialValue = false;

    /**
     * {@inheritDoc}
     */
    public void validateState() throws ApplicationRuntimeException {
        ValidatableUtils.validateObjectIsNotNull(
                getFeatureEnablementRegistry(), "featureEnablementRegistry");
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        logger.info("Destroying ...");
    }

    /**
     * {@inheritDoc}
     *
     * See class docs for {@link #BundleExploderActivatonFilter()}
     * .
     */
    public void doFilter(final ServletRequest servletRequest,
            final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        if (!HttpServletRequest.class.isInstance(servletRequest)) {
            throw new IllegalArgumentException(
                    "Request must be an instance of HttpServletRequest but was: "
                            + servletRequest);
        }

        if (!HttpServletResponse.class.isInstance(servletResponse)) {
            throw new IllegalArgumentException(
                    "Response must be an instance of HttpServletResponse but was: "
                            + servletResponse);
        }

        final HttpServletRequest httpServletRequest =
                (HttpServletRequest) servletRequest;

        if (isNewSession(httpServletRequest)) {
            setInitialBundleExploderFlag(httpServletRequest);
        } else if (isBundleExploderRequestedNonNullAndTrue(httpServletRequest)) {
            activateBundleExploderRequestInSession(httpServletRequest);
        } else if (isBundleExploderRequestedNonNullAndFalse(httpServletRequest)) {
            deactivateBundleExploderRequestInSession(httpServletRequest);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setInitialBundleExploderFlag(
            final HttpServletRequest httpServletRequest) {
        if (isBundleExplosionInitialValue()
                && getFeatureEnablementRegistry().isBundleExplosionEnabled()) {
            activateBundleExploderRequestInSession(httpServletRequest);
        }
    }

    private boolean isNewSession(final HttpServletRequest httpServletRequest) {
        return httpServletRequest.getSession().isNew();
    }

    /**
     * @param httpServletRequest
     */
    private void activateBundleExploderRequestInSession(
            final HttpServletRequest httpServletRequest) {
        if (getFeatureEnablementRegistry().isBundleExplosionEnabled()) {
        logger.info("Activating bundle explosion.");
        httpServletRequest
                .getSession()
                .setAttribute(
                        ResourceBundleLoaderController.BUNDLE_EXPLODER_REQUEST_SESSION_KEY,
                        Boolean.TRUE);
        } else {
            logger.warn(
                "Support for bundle explosion is disabled. Ignoring bundle explosion request.");
        }
    }

    /**
     * @param httpServletRequest
     */
    private void deactivateBundleExploderRequestInSession(
            final HttpServletRequest httpServletRequest) {
        logger.info("Deactivating bundle explosion.");
        httpServletRequest
                .getSession()
                .setAttribute(
                        ResourceBundleLoaderController.BUNDLE_EXPLODER_REQUEST_SESSION_KEY,
                        Boolean.FALSE);
    }

    private boolean isBundleExploderRequestedNull(
            final HttpServletRequest httpServletRequest) {
        return httpServletRequest
                .getParameter(BUNDLE_EXPLODER_REQUEST_PARAM_NAME) == null;
    }

    private boolean isBundleExploderRequestedNonNullAndTrue(
            final HttpServletRequest httpServletRequest) {
        return BOOLEAN_TRUE_PARAM_VALUE.equals(httpServletRequest
                .getParameter(BUNDLE_EXPLODER_REQUEST_PARAM_NAME));
    }

    private boolean isBundleExploderRequestedNonNullAndFalse(
            final HttpServletRequest httpServletRequest) {
        return !isBundleExploderRequestedNull(httpServletRequest)
                && !BOOLEAN_TRUE_PARAM_VALUE.equals(httpServletRequest
                        .getParameter(BUNDLE_EXPLODER_REQUEST_PARAM_NAME));
    }

    /**
     * {@inheritDoc}
     */
    public void init(final FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing ...");
    }

    /**
     * @return the featureEnablementRegistry
     */
    public FeatureEnablementRegistry getFeatureEnablementRegistry() {
        return featureEnablementRegistry;
    }

    /**
     * @param featureEnablementRegistry
     *            the featureEnablementRegistry to set
     */
    public void setFeatureEnablementRegistry(
            final FeatureEnablementRegistry featureEnablementRegistry) {
        this.featureEnablementRegistry = featureEnablementRegistry;
    }

    /**
     * @return the bundleExplosionInitialValue
     */
    public boolean isBundleExplosionInitialValue() {
        return bundleExplosionInitialValue;
    }

    /**
     * @param bundleExplosionInitialValue the bundleExplosionInitialValue to set
     */
    public void setBundleExplosionInitialValue(final boolean bundleExplosionInitialValue) {
        this.bundleExplosionInitialValue = bundleExplosionInitialValue;
    }
}
