package au.com.sensis.mobile.web.component.core.bundle;

/**
 * {@link FeatureEnablementRegistry} that expects flags to be dependency
 * injected. By default, all flags are false.
 */
public class SimpleFeatureEnablementRegistryBean implements
        FeatureEnablementRegistry {

    private boolean bundleExplosionEnabled = false;
    private boolean bypassClientCacheEnabled = false;

    /**
     * {@inheritDoc}
     */
    public boolean isBundleExplosionEnabled() {
        return bundleExplosionEnabled;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBypassClientCacheEnabled() {
        return bypassClientCacheEnabled;
    }

    /**
     * @param bundleExplosionEnabled the bundleExplosionEnabled to set
     */
    public void setBundleExplosionEnabled(final boolean bundleExplosionEnabled) {
        this.bundleExplosionEnabled = bundleExplosionEnabled;
    }

    /**
     * @param bypassClientCacheEnabled the bypassClientCacheEnabled to set
     */
    public void setBypassClientCacheEnabled(final boolean bypassClientCacheEnabled) {
        this.bypassClientCacheEnabled = bypassClientCacheEnabled;
    }
}
