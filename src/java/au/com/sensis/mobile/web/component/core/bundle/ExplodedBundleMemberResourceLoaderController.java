package au.com.sensis.mobile.web.component.core.bundle;


import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;


/**
 * {@link AbstractResourceBundleLoaderController} that delegates to
 * {@link ResourceBundleLoader#loadExplodedBundleMember(String)}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ExplodedBundleMemberResourceLoaderController extends
        AbstractResourceBundleLoaderController {

    // Not final so that we can inject a mock during unit tests.
    private static Logger logger =
            Logger.getLogger(ExplodedBundleMemberResourceLoaderController.class);

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    protected ModelAndView handleRequestInternal(
            final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Loading exploded bundle member for request: '"
                    + request.getRequestURI() + "'");
        }

        final InputStream bundleInputStream =
                getResourceBundleLoader().loadExplodedBundleMember(
                        extractResourceNameRequested(request));

        writeInputStreamToResponse(response, bundleInputStream);

        return null;
    }

    /**
     * @return the logger
     */
    @Override
    protected Logger getLogger() {
        return logger;
    }

    /**
     * {@inheritDoc}
     */
    public long getLastModified(final HttpServletRequest request) {
        if (isByassClientCacheRequested(request)) {
            return LAST_MODIFIED_UNKNOWN;
        } else {
            try {
                return getResourceBundleLoader()
                        .getExplodedBundleMemberLastModified(
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
}
