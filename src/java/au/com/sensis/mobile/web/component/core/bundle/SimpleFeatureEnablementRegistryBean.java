package au.com.sensis.mobile.web.component.core.bundle;

/**
 * {@link FeatureEnablementRegistry} that expects flags to be dependency
 * injected. By default, all flags are false.
 */
public class SimpleFeatureEnablementRegistryBean implements
        FeatureEnablementRegistry {

    private boolean bundleExplosionEnabled = false;

    /**
     * {@inheritDoc}
     */
    public boolean isBundleExplosionEnabled() {
        return bundleExplosionEnabled;
    }

    /**
     * @param bundleExplosionEnabled the bundleExplosionEnabled to set
     */
    public void setBundleExplosionEnabled(final boolean bundleExplosionEnabled) {
        this.bundleExplosionEnabled = bundleExplosionEnabled;
    }
}
