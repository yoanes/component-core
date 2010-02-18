package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Invokes a chain of {@link ResourceBundleLoader}s, stopping with the first one
 * that successfully handles the request.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ChainedResourceBundleLoader implements ResourceBundleLoader {

    private static Logger logger =
            Logger.getLogger(ChainedResourceBundleLoader.class);

    private List<ResourceBundleLoader> resourceBundleLoaders =
            new ArrayList<ResourceBundleLoader>();

    /**
     * Default constructor.
     *
     * @param resourceBundleLoaders
     *            Chain of {@link ResourceBundleLoader}s to be delegated to.
     */
    public ChainedResourceBundleLoader(
            final List<ResourceBundleLoader> resourceBundleLoaders) {
        if ((resourceBundleLoaders == null) || resourceBundleLoaders.isEmpty()) {
            throw new IllegalArgumentException(
                    "resourceBundleLoaders must be non-empty. Was: "
                            + resourceBundleLoaders);
        }
        this.resourceBundleLoaders = resourceBundleLoaders;
    }

    /**
     * {@inheritDoc}
     */
    public long getBundleExploderLastModified(final String bundleName)
            throws IOException {
        return walkResourceBundleLoaderChain(new DelegateCall<Long>() {
                public Long call(
                    final ResourceBundleLoader resourceBundleLoader,
                    final String resourceName) throws IOException {
                return resourceBundleLoader
                        .getBundleExploderLastModified(bundleName);
            };
        }, bundleName);
    }

    /**
     * {@inheritDoc}
     */
    public long getBundleLastModified(final String bundleName)
            throws IOException {
        return walkResourceBundleLoaderChain(new DelegateCall<Long>() {
            public Long call(final ResourceBundleLoader resourceBundleLoader,
                    final String resourceName) throws IOException {
                return resourceBundleLoader
                        .getBundleLastModified(bundleName);
            };
        }, bundleName);

    }

    /**
     * {@inheritDoc}
     */
    public long getExplodedBundleMemberLastModified(
            final String explodedBundleMemberName) throws IOException {
        return walkResourceBundleLoaderChain(new DelegateCall<Long>() {
            public Long call(final ResourceBundleLoader resourceBundleLoader,
                    final String resourceName) throws IOException {
                return resourceBundleLoader
                        .getExplodedBundleMemberLastModified(explodedBundleMemberName);
            };
        }, explodedBundleMemberName);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream loadBundle(final String bundleName) throws IOException {
        return walkResourceBundleLoaderChain(new DelegateCall<InputStream>() {
            public InputStream call(
                    final ResourceBundleLoader resourceBundleLoader,
                    final String resourceName) throws IOException {
                return resourceBundleLoader.loadBundle(bundleName);
            };
        }, bundleName);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream loadBundleExploder(final String bundleName)
            throws IOException {
        return walkResourceBundleLoaderChain(new DelegateCall<InputStream>() {
            public InputStream call(
                    final ResourceBundleLoader resourceBundleLoader,
                    final String resourceName) throws IOException {
                return resourceBundleLoader.loadBundleExploder(bundleName);
            };
        }, bundleName);
    }

    /**
     * {@inheritDoc}
     */
    public InputStream loadExplodedBundleMember(
            final String explodedBundleMemberName) throws IOException {
        return walkResourceBundleLoaderChain(new DelegateCall<InputStream>() {
            public InputStream call(
                    final ResourceBundleLoader resourceBundleLoader,
                    final String resourceName) throws IOException {
                return resourceBundleLoader.loadExplodedBundleMember(explodedBundleMemberName);
            };
        }, explodedBundleMemberName);
    }

    /**
     * @return the resourceBundleLoaders
     */
    private List<ResourceBundleLoader> getResourceBundleLoaders() {
        return resourceBundleLoaders;
    }

    private boolean isLastResourceBundleLoaderInChain(final int i) {
        return i == getResourceBundleLoaders().size() - 1;
    }

    private void logIntermediateResourceBundleLoaderFailedMessage(
            final String bundleName, final int i, final Exception e) {
        logger.warn("ResourceBundleLoader at index " + i + " threw an "
                + "exception for requested bundle '"
                + bundleName
                + "'. Will try next ResourceBundleLoader "
                + "in the chain.", e);
    }

    /**
     * @param bundleName
     * @param i
     * @param e
     * @return
     */
    private IOException createLastResourceBundleLoaderFailedException(
            final String bundleName, final int i, final Exception e) {
        final IOException ioException =
                new IOException(
                        "The last ResourceBundleLoader in the chain (index "
                                + i + ") threw an exception "
                                + "for requested bundle '" + bundleName
                                + "'. Aborting.");
        ioException.initCause(e);
        return ioException;
    }

    private interface DelegateCall<T> {
        T call(ResourceBundleLoader resourceBundleLoader,
                String resourceName) throws IOException;
    }

    /**
     * Iterates through the chain of {@link ResourceBundleLoader}s and delegates
     * to it using the call encapsulated by the given {@link DelegateCall}
     * interface.
     *
     * @param <T>
     *            Type that that the delegated method will return.
     * @param delegateCall
     *            Encapsulates the call that will be delegated to the
     *            {@link ResourceBundleLoader}.
     * @param resourceName
     *            Name of the resource being requested to be loaded.
     * @return Instance of the type that the {@link DelegateCall} returns.
     * @throws IOException
     *             Thrown if no {@link ResourceBundleLoader} in the chain
     *             successfully loaded the resource.
     */
    private <T> T walkResourceBundleLoaderChain(final DelegateCall<T> delegateCall,
            final String resourceName) throws IOException {
        int i = 0;
        for (final ResourceBundleLoader resourceBundleLoader : getResourceBundleLoaders()) {
            try {
                return delegateCall.call(resourceBundleLoader, resourceName);
            } catch (final Exception e) {
                if (isLastResourceBundleLoaderInChain(i)) {
                    final IOException ioException =
                            createLastResourceBundleLoaderFailedException(
                                    resourceName, i, e);
                    throw ioException;
                } else {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logIntermediateResourceBundleLoaderFailedMessage(
                                resourceName, i, e);
                    }
                    ++i;
                }
            }
        }
        throw new IllegalStateException(
                "Reached the end of walkResourceBundleLoaderChain without returning a "
                        + "value or throwing an exception. This should never happen !!!");

    }
}
