package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;

import au.com.sensis.wireless.web.common.exception.ApplicationRuntimeException;
import au.com.sensis.wireless.web.common.validation.Validatable;
import au.com.sensis.wireless.web.common.validation.ValidatableUtils;

/**
 * Base implementation of {@link ResourceBundleLoader} that delegates the
 * ultimate file lookup call to the {@link #loadFile(String)} template method.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public abstract class AbstractResourceBundleLoader implements
        ResourceBundleLoader, Validatable {

    private String bundleBasePath;
    private String bundleExploderBasePath;
    private String explodedBundleMemberBasePath;

    /**
     * {@inheritDoc}
     */
    public void validateState() throws ApplicationRuntimeException {
        ValidatableUtils.validateStringIsNotBlank(getBundleBasePath(),
                "bundleBasePath");
        ValidatableUtils.validateStringIsNotBlank(getBundleExploderBasePath(),
                "bundleExploderBasePath");
        ValidatableUtils.validateStringIsNotBlank(
                getExplodedBundleMemberBasePath(), "explodedBundleMemberBasePath");

    }

    /**
     * {@inheritDoc}
     */
    public final InputStream loadBundle(final String bundleName)
        throws IOException {
        return loadFile(getBundlePath(bundleName));
    }

    /**
     * {@inheritDoc}
     */
    public final long getBundleLastModified(final String bundleName)
            throws IOException {
        return getFileLastModified(getBundlePath(bundleName));
    }

    /**
     * {@inheritDoc}
     */
    public final InputStream loadBundleExploder(final String bundleName)
        throws IOException {
        return loadFile(getBundleExploderPath(bundleName));
    }

    /**
     * {@inheritDoc}
     */
    public long getBundleExploderLastModified(final String bundleName)
            throws IOException {
        return getFileLastModified(getBundleExploderPath(bundleName));
    }

    /**
     * {@inheritDoc}
     */
    public final InputStream loadExplodedBundleMember(
            final String explodedJavaScriptFilename)
        throws IOException {
        return loadFile(getExplodedBundleMemberPath(explodedJavaScriptFilename));

    }

    /**
     * {@inheritDoc}
     */
    public long getExplodedBundleMemberLastModified(
            final String explodedBundleMemberName) throws IOException {
        return getFileLastModified(getExplodedBundleMemberPath(explodedBundleMemberName));
    }

    /**
     * Load the file specified by the given path. The meaning of the path is
     * implementation dependent. eg. it may be a path on class path or relative
     * to a web app context root etc.
     *
     * @param filePath Path of the file to be loaded.
     * @return {@link InputStream} of the loaded file.
     * @throws IOException Thrown if the file could not be found.
     */
    protected abstract InputStream loadFile(String filePath)
        throws IOException;

    /**
     * Time the file specified by the given path was last modified, using the
     * same value semantics as {@link java.util.Date#getTime()}. The meaning of
     * the path is implementation dependent. eg. it may be a path on class path
     * or relative to a web app context root etc.
     *
     * @param filePath
     *            Path of the file to be loaded.
     * @return Time the file specified by the given path was last modified,
     *         using the same value semantics as
     *         {@link java.util.Date#getTime()}. The meaning of the path is
     *         implementation dependent. eg. it may be a path on class path or
     *         relative to a web app context root etc.
     * @throws IOException
     *             Thrown if there was an error in determining the last modified time.
     */
    protected abstract long getFileLastModified(String filePath)
            throws IOException;

    /**
     * @param bundleName name of the bundle to get a path for.
     * @return path to the given bundle that can be passed to {@link #loadFile(String)}.
     */
    protected final String getBundlePath(final String bundleName) {
        return getBundleBasePath() + bundleName;
    }

    /**
     * @param bundleName name of the bundle to get a path for.
     * @return path to the given bundle that can be passed to {@link #loadFile(String)}.
     */
    protected final String getBundleExploderPath(final String bundleName) {
        return getBundleExploderBasePath() + bundleName;
    }

    /**
     * @param explodedBundleMemberName name of the bundle to get a path for.
     * @return path to the given bundle that can be passed to {@link #loadFile(String)}.
     */
    protected final String getExplodedBundleMemberPath(
            final String explodedBundleMemberName) {
        return getExplodedBundleMemberBasePath() + explodedBundleMemberName;
    }

    /**
     * @return the base path for looking up bundle names passed to
     *         {@link #loadBundle(String)}. The meaning of the path depends on
     *         the {@link #loadFile(String)} implementation.
     */
    protected final String getBundleBasePath() {
        return bundleBasePath;
    }

    /**
     * @param bundleBasePath
     *            the base path for looking up bundle names passed to
     *            {@link #loadBundle(String)}. The meaning of the path depends
     *            on the {@link #loadFile(String)} implementation.
     */
    public final void setBundleBasePath(final String bundleBasePath) {
        this.bundleBasePath = bundleBasePath;
    }

    /**
     * @return the base path for looking up bundle names passed to
     *         {@link #loadBundleExploder(String)}. The meaning of the path
     *         depends on the {@link #loadFile(String)} implementation.
     */
    public final String getBundleExploderBasePath() {
        return bundleExploderBasePath;
    }

    /**
     * @param bundleExploderBasePath
     *            the base path for looking up bundle names passed to
     *            {@link #loadBundleExploder(String)}. The meaning of the path
     *            depends on the {@link #loadFile(String)} implementation.
     */
    public final void setBundleExploderBasePath(
            final String bundleExploderBasePath) {
        this.bundleExploderBasePath = bundleExploderBasePath;
    }

    /**
     * @return the base path for looking up bundle member names passed to
     *         {@link #loadExplodedBundleMember(String)}. The meaning of the path
     *         depends on the {@link #loadFile(String)} implementation.
     */
    public final String getExplodedBundleMemberBasePath() {
        return explodedBundleMemberBasePath;
    }

    /**
     * @param explodedBundleMemberBasePath
     *            the base path for looking up bundle member names passed to
     *            {@link #loadExplodedBundleMember(String)}. The meaning of the
     *            path depends on the {@link #loadFile(String)} implementation.
     */
    public final void setExplodedBundleMemberBasePath(
            final String explodedBundleMemberBasePath) {
        this.explodedBundleMemberBasePath = explodedBundleMemberBasePath;
    }

}
