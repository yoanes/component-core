package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for loading bundles of web client resources (such as JavaScript and
 * CSS). eg. a bundle of compressed JavaScript. Also provides methods for
 * loading the bundles in exploded form.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public interface ResourceBundleLoader {

    /**
     * Load a bundle by name.
     *
     * @param bundleName
     *            Name of the bundle. The mapping of the name to actual bundles
     *            is implementation dependent.
     * @return the requested bundle.
     * @throws IOException Thrown if the bundle could not be found.
     */
    InputStream loadBundle(String bundleName) throws IOException;

    /**
     * Time that the bundle that would be loaded by {@link #loadBundle(String)}
     * was last modified, using the same value semantics as
     * {@link java.util.Date#getTime()}.
     *
     * @param bundleName
     *            Name of the bundle. The mapping of the name to actual bundles
     *            is implementation dependent.
     * @return Time that the bundle that would be loaded by
     *         {@link #loadBundle(String)} was last modified, using the same
     *         value semantics as {@link java.util.Date#getTime()}.
     * @throws IOException
     *             Thrown if the bundle could not be found.
     */
    long getBundleLastModified(String bundleName) throws IOException;

    /**
     * Load a version of the bundle that simply knows how to subsequently
     * retrieve exploded members of the bundle. Mainly intended for debugging
     * purposes in a dev environment.
     *
     * @param bundleName
     *            Name of the bundle. The mapping of the name to actual bundles
     *            is implementation dependent.
     * @return a version of the bundle that simply knows how to subsequently
     *         retrieve exploded members of the bundle.
     * @throws IOException Thrown if the bundle could not be found.
     */
    InputStream loadBundleExploder(String bundleName) throws IOException;

    /**
     * Time that the bundle that would be loaded by
     * {@link #loadBundleExploder(String)} was last modified, using the same
     * value semantics as {@link java.util.Date#getTime()}.
     *
     * @param bundleName
     *            Name of the bundle. The mapping of the name to actual bundles
     *            is implementation dependent.
     * @return Time that the bundle that would be loaded by
     *         {@link #loadBundleExploder(String)} was last modified, using the
     *         same value semantics as {@link java.util.Date#getTime()}.
     * @throws IOException
     *             Thrown if the bundle could not be found.
     */
    long getBundleExploderLastModified(String bundleName)
            throws IOException;

    /**
     * Load a single member of an exploded bundle. Mainly intended for debugging
     * purposes in a dev environment.
     *
     * @param explodedBundleMemberName
     *            Name of the bundle's member. The mapping of the name to actual
     *            bundle members is implementation dependent.
     * @return a single member of an exploded bundle.
     * @throws IOException
     *             Thrown if the explodedBundleMemberName could not be found.
     */
    InputStream loadExplodedBundleMember(String explodedBundleMemberName)
            throws IOException;

    /**
     * Time that the explodedBundleMemberName that would be loaded by
     * {@link #loadExplodedBundleMember(String)} was last modified, using the same
     * value semantics as {@link java.util.Date#getTime()}.
     *
     * @param explodedBundleMemberName
     *            Name of the bundle. The mapping of the name to actual bundles
     *            is implementation dependent.
     * @return Time that the explodedBundleMemberName that would be loaded by
     *         {@link #loadExplodedBundleMember(String)} was last modified, using the
     *         same value semantics as {@link java.util.Date#getTime()}.
     * @throws IOException
     *             Thrown if the bundle could not be found.
     */
    long getExplodedBundleMemberLastModified(String explodedBundleMemberName)
        throws IOException;

}
