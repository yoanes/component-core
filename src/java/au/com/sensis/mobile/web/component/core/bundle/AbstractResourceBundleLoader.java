package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

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

    /**
     * The regex to match against the bundle name passed to {@link #loadBundle(String)}
     * or {@link #loadBundleExploder(String)}. See {@link #getBundleNameReplacement()}
     * and {@link #getBundleExploderNameReplacement()}.
     */
    private String bundleNameRegex;

    /**
     * The replacement String to use against the {@link #getBundleNameRegex()}
     * when {@link #loadBundle(String)} is called. Note that this replacement
     * String can contain usual regex capture group references.
     */
    private String bundleNameReplacement;

    /**
     * The replacement String to use against the {@link #getBundleNameRegex()}
     * when {@link #loadBundleExploder(String)} is called. Note that this replacement
     * String can contain usual regex capture group references.
     */
    private String bundleExploderNameReplacement;

    /**
     * The regex to match against the exploded bundle member name passed to
     * {@link #loadExplodedBundleMember(String)}.
     * See {@link #getExplodedBundleMemberNameReplacement()}.
     */
    private String explodedBundleMemberNameRegex;

    /**
     * The replacement String to use against the {@link #getExplodedBundleMemberNameRegex()}
     * when {@link #loadExplodedBundleMember(String)} is called. Note that this replacement
     * String can contain usual regex capture group references.
     */
    private String explodedBundleMemberNameReplacement;

    /**
     * {@inheritDoc}
     */
    public void validateState() throws ApplicationRuntimeException {
        ValidatableUtils.validateStringIsNotBlank(getBundleNameRegex(),
                "bundleNameRegex");
        ValidatableUtils.validateStringIsNotBlank(getBundleNameReplacement(),
                "bundleNameReplacement");
        ValidatableUtils.validateStringIsNotBlank(
                getBundleExploderNameReplacement(), "bundleExploderNameReplacement");
        ValidatableUtils.validateStringIsNotBlank(
                getExplodedBundleMemberNameRegex(), "explodedBundleMemberNameRegex");
        ValidatableUtils.validateStringIsNotBlank(
                getExplodedBundleMemberNameReplacement(), "explodedBundleMemberNameReplacement");
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
        if (bundleName != null) {
            return bundleName.replaceFirst(getBundleNameRegex(), getBundleNameReplacement());
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * @param bundleName name of the bundle to get a path for.
     * @return path to the given bundle that can be passed to {@link #loadFile(String)}.
     */
    protected final String getBundleExploderPath(final String bundleName) {
        if (bundleName != null) {
            return bundleName.replaceFirst(getBundleNameRegex(),
                    getBundleExploderNameReplacement());
        } else {
            return StringUtils.EMPTY;
        }

    }

    /**
     * @param explodedBundleMemberName name of the bundle to get a path for.
     * @return path to the given bundle that can be passed to {@link #loadFile(String)}.
     */
    protected final String getExplodedBundleMemberPath(
            final String explodedBundleMemberName) {
        if (explodedBundleMemberName != null) {
            return explodedBundleMemberName.replaceFirst(getExplodedBundleMemberNameRegex(),
                    getExplodedBundleMemberNameReplacement());
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * The regex to match against the bundle name passed to
     * {@link #loadBundle(String)} or {@link #loadBundleExploder(String)}. See
     * {@link #getBundleNameReplacement()} and
     * {@link #getBundleExploderNameReplacement()}.
     *
     * @return The regex to match against the bundle name passed to
     *         {@link #loadBundle(String)} or
     *         {@link #loadBundleExploder(String)}. See
     *         {@link #getBundleNameReplacement()} and
     *         {@link #getBundleExploderNameReplacement()}.
     */
    public final String getBundleNameRegex() {
        return bundleNameRegex;
    }

    /**
     * The regex to match against the bundle name passed to
     * {@link #loadBundle(String)} or {@link #loadBundleExploder(String)}. See
     * {@link #getBundleNameReplacement()} and
     * {@link #getBundleExploderNameReplacement()}.
     *
     * @param bundleNameRegex
     *            The regex to match against the bundle name passed to
     *            {@link #loadBundle(String)} or
     *            {@link #loadBundleExploder(String)}. See
     *            {@link #getBundleNameReplacement()} and
     *            {@link #getBundleExploderNameReplacement()}.
     */
    public final void setBundleNameRegex(final String bundleNameRegex) {
        this.bundleNameRegex = bundleNameRegex;
    }

    /**
     * The replacement String to use against the {@link #getBundleNameRegex()}
     * when {@link #loadBundle(String)} is called. Note that this replacement
     * String can contain usual regex capture group references.
     *
     * @return the bundleNameReplacement The replacement String to use against
     *         the {@link #getBundleNameRegex()} when
     *         {@link #loadBundle(String)} is called. Note that this replacement
     *         String can contain usual regex capture group references.
     */
    public final String getBundleNameReplacement() {
        return bundleNameReplacement;
    }

    /**
     * The replacement String to use against the {@link #getBundleNameRegex()}
     * when {@link #loadBundle(String)} is called. Note that this replacement
     * String can contain usual regex capture group references.
     *
     * @param bundleNameReplacement
     *            The replacement String to use against the
     *            {@link #getBundleNameRegex()} when {@link #loadBundle(String)}
     *            is called. Note that this replacement String can contain usual
     *            regex capture group references.
     */
    public final void setBundleNameReplacement(
            final String bundleNameReplacement) {
        this.bundleNameReplacement = bundleNameReplacement;
    }

    /**
     * The replacement String to use against the {@link #getBundleNameRegex()}
     * when {@link #loadBundleExploder(String)} is called. Note that this
     * replacement String can contain usual regex capture group references.
     *
     * @return The replacement String to use against the
     *         {@link #getBundleNameRegex()} when
     *         {@link #loadBundleExploder(String)} is called. Note that this
     *         replacement String can contain usual regex capture group
     *         references.
     */
    public final String getBundleExploderNameReplacement() {
        return bundleExploderNameReplacement;
    }

    /**
     * The replacement String to use against the {@link #getBundleNameRegex()}
     * when {@link #loadBundleExploder(String)} is called. Note that this
     * replacement String can contain usual regex capture group references.
     *
     * @param bundleExploderNameReplacement
     *            The replacement String to use against the
     *            {@link #getBundleNameRegex()} when
     *            {@link #loadBundleExploder(String)} is called. Note that this
     *            replacement String can contain usual regex capture group
     *            references.
     */
    public final void setBundleExploderNameReplacement(
            final String bundleExploderNameReplacement) {
        this.bundleExploderNameReplacement = bundleExploderNameReplacement;
    }

    /**
     * The regex to match against the exploded bundle member name passed to
     * {@link #loadExplodedBundleMember(String)}. See
     * {@link #getExplodedBundleMemberNameReplacement()}.
     *
     * @return The regex to match against the exploded bundle member name passed
     *         to {@link #loadExplodedBundleMember(String)}. See
     *         {@link #getExplodedBundleMemberNameReplacement()}.
     */
    public final String getExplodedBundleMemberNameRegex() {
        return explodedBundleMemberNameRegex;
    }

    /**
     * The regex to match against the exploded bundle member name passed to
     * {@link #loadExplodedBundleMember(String)}. See
     * {@link #getExplodedBundleMemberNameReplacement()}.
     *
     * @param explodedBundleMemberNameRegex
     *            The regex to match against the exploded bundle member name
     *            passed to {@link #loadExplodedBundleMember(String)}. See
     *            {@link #getExplodedBundleMemberNameReplacement()}.
     */
    public final void setExplodedBundleMemberNameRegex(
            final String explodedBundleMemberNameRegex) {
        this.explodedBundleMemberNameRegex = explodedBundleMemberNameRegex;
    }

    /**
     * The replacement String to use against the
     * {@link #getExplodedBundleMemberNameRegex()} when
     * {@link #loadExplodedBundleMember(String)} is called. Note that this
     * replacement String can contain usual regex capture group references.
     *
     * @return The replacement String to use against the
     *         {@link #getExplodedBundleMemberNameRegex()} when
     *         {@link #loadExplodedBundleMember(String)} is called. Note that
     *         this replacement String can contain usual regex capture group
     *         references.
     */
    public final String getExplodedBundleMemberNameReplacement() {
        return explodedBundleMemberNameReplacement;
    }

    /**
     * The replacement String to use against the
     * {@link #getExplodedBundleMemberNameRegex()} when
     * {@link #loadExplodedBundleMember(String)} is called. Note that this
     * replacement String can contain usual regex capture group references.
     *
     * @param explodedBundleMemberNameReplacement
     *            The replacement String to use against the
     *            {@link #getExplodedBundleMemberNameRegex()} when
     *            {@link #loadExplodedBundleMember(String)} is called. Note that
     *            this replacement String can contain usual regex capture group
     *            references.
     */
    public final void setExplodedBundleMemberNameReplacement(
            final String explodedBundleMemberNameReplacement) {
        this.explodedBundleMemberNameReplacement =
                explodedBundleMemberNameReplacement;
    }

}
