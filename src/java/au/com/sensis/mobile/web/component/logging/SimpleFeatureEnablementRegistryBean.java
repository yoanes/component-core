package au.com.sensis.mobile.web.component.logging;

/**
 * {@link FeatureEnablementRegistry} that expects flags to be dependency
 * injected. By default, all flags are false.
 */
public class SimpleFeatureEnablementRegistryBean implements
        FeatureEnablementRegistry {

    private boolean javaScriptLoggerEnabled = false;

    /**
     * {@inheritDoc}
     */
    public boolean isJavaScriptLoggerEnabled() {
        return javaScriptLoggerEnabled;
    }

    /**
     * @param javaScriptLoggerEnabled the javaScriptLoggerEnabled to set
     */
    public void setJavaScriptLoggerEnabled(final boolean javaScriptLoggerEnabled) {
        this.javaScriptLoggerEnabled = javaScriptLoggerEnabled;
    }

}
