package au.com.sensis.mobile.web.component.core.bundle;

/**
 * Registry of flags that indicate whether particular features are enabled/disabled.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public interface FeatureEnablementRegistry {

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
