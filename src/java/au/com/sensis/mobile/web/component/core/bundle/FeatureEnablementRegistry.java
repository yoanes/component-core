package au.com.sensis.mobile.web.component.core.bundle;

/**
 * Registry of flags that indicate whether particular features are enabled/disabled.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public interface FeatureEnablementRegistry {

    /**
     * If true, classes that support bypassing of a client's cache (eg.
     * controllers that ignore the HTTP If-Modified-Since header) will have this
     * support enabled. This should typically only be set to true for desktop
     * and sandpit environments. If {@link #isBundleExplosionEnabled()} is true,
     * then {@link #isBypassClientCacheEnabled()} should usually be true as
     * well, otherwise a cached response may be returned even if you toggle the
     * "bundle"/"exploded bundle" request switch.
     *
     * @return If true, classes that support bypassing of a client's cache (eg.
     *         controllers that ignore the HTTP If-Modified-Since header) will
     *         have this support enabled.
     */
    boolean isBypassClientCacheEnabled();

    /**
     * If true, classes that support explosion of bundles will have this support
     * enabled. This should typically only be set to true for desktop and
     * sandpit environments.
     *
     * @return If true, classes that support explosion of bundles will have this
     *         support enabled.
     */
    boolean isBundleExplosionEnabled();
}
