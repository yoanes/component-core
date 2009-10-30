package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import au.com.sensis.wireless.web.common.exception.ApplicationRuntimeException;
import au.com.sensis.wireless.web.common.validation.ValidatableUtils;

/**
 * {@link AbstractResourceBundleLoaderController} that delegates to either
 * {@link au.com.sensis.mobile.web.component.core.bundle.ResourceBundleLoader#loadBundle(String)} or
 * {@link au.com.sensis.mobile.web.component.core.bundle.ResourceBundleLoader
 * #loadBundleExploder(String)},
 * depending on whether {@link FeatureEnablementRegistry#isBundleExplosionEnabled()} and whether the
 * {@link #BUNDLE_EXPLODER_REQUEST_SESSION_KEY} session key is set.
 *
 *
 * @author Adrian.Koh2@sensis.com.au
 *
 */
public class ResourceBundleLoaderController extends
        AbstractResourceBundleLoaderController {

    private static final long serialVersionUID = 1L;

    // Not final so that we can inject a mock during unit tests.
    private static Logger logger =
            Logger.getLogger(ResourceBundleLoaderController.class);

    /**
     * Name of the session key that requests that the bundle exploder be
     * returned instead of the bundle itself.
     */
    public static final String BUNDLE_EXPLODER_REQUEST_SESSION_KEY
        = ResourceBundleLoaderController.class.getName() + ".bundleExploderRequest";

    private String contextRootComponentPrefix;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateState() throws ApplicationRuntimeException {
        super.validateState();

        ValidatableUtils
                .validateStringIsNotBlank(getContextRootComponentPrefix(),
                        "contextRootComponentPrefix");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ModelAndView handleRequestInternal(
            final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        InputStream bundleInputStream;
        if (isBundleExploderRequested(request)) {

            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Loading bundleExploder for request: '"
                        + request.getRequestURI() + "'");
            }

            bundleInputStream =
                    getResourceBundleLoader().loadBundleExploder(
                            extractResourceNameRequested(request));

            final PrintWriter printWriter = response.getWriter();
            printWriter.println("var jsPathPrefix='"
                    + getContextRootComponentPrefix() + "';");
        } else {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Loading bundle for request: '"
                        + request.getRequestURI() + "'");
            }

            bundleInputStream =
                    getResourceBundleLoader().loadBundle(
                            extractResourceNameRequested(request));
        }

        writeInputStreamToResponse(response, bundleInputStream);

        return null;
    }

    private boolean isBundleExploderRequested(final HttpServletRequest request) {
        if (getFeatureEnablementRegistry().isBundleExplosionEnabled()) {
            final Object bundleExploderRequestedSessionValue =
                    request.getSession().getAttribute(
                            BUNDLE_EXPLODER_REQUEST_SESSION_KEY);
            return (bundleExploderRequestedSessionValue != null)
                    && Boolean.TRUE.equals(bundleExploderRequestedSessionValue);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getLastModified(final HttpServletRequest request) {
        if (isBundleExploderRequested(request)) {
            return getBundleExploderLastModified(request);
        } else {
            return getBundleLastModified(request);

        }
    }

    /**
     * @param request
     * @return
     * @throws IOException
     */
    private long getBundleLastModified(final HttpServletRequest request) {
        if (isByassClientCacheRequested(request)) {
            return LAST_MODIFIED_UNKNOWN;
        } else {
            try {
                return getResourceBundleLoader().getBundleLastModified(
                        extractResourceNameRequested(request));
            } catch (final IOException e) {
                if (getLogger().isEnabledFor(Level.WARN)) {
                    getLogger().warn("Could not tell if bundle '"
                            + extractResourceNameRequested(request)
                            + "' was modified. Considering it to have been modified.", e);
                }
                return LAST_MODIFIED_UNKNOWN;
            }
        }
    }

    /**
     * @param request
     * @return
     * @throws IOException
     */
    private long getBundleExploderLastModified(final HttpServletRequest request) {
        if (isByassClientCacheRequested(request)) {
            return LAST_MODIFIED_UNKNOWN;
        } else {
            try {
                return getResourceBundleLoader().getBundleExploderLastModified(
                        extractResourceNameRequested(request));
            } catch (final IOException e) {
                if (getLogger().isEnabledFor(Level.WARN)) {
                    getLogger().warn("Could not tell if bundle '"
                            + extractResourceNameRequested(request)
                            + "' was modified. Considering it to have been modified.", e);
                }

                return LAST_MODIFIED_UNKNOWN;
            }

        }
    }

    /**
     * @return the logger
     */
    @Override
    protected Logger getLogger() {
        return logger;
    }

    /**
     * @return the contextRootComponentPrefix
     */
    public String getContextRootComponentPrefix() {
        return contextRootComponentPrefix;
    }

    /**
     * @param contextRootComponentPrefix
     *            the contextRootComponentPrefix to set
     */
    public void setContextRootComponentPrefix(
            final String contextRootComponentPrefix) {
        this.contextRootComponentPrefix = contextRootComponentPrefix;
    }
}
