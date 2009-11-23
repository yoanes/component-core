package au.com.sensis.mobile.web.component.logging;

/**
 * Registry of flags that indicate whether paritcular features are enabled/disabled.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public interface FeatureEnablementRegistry {

    /**
     * If true, the JavaScript logger will be enabled. This should typically
     * only be set to true for desktop and sandpit environments.
     *
     * @return If true, the JavaScript logger will be enabled. This should
     *         typically only be set to true for desktop and sandpit
     *         environments.
     */
    boolean isJavaScriptLoggerEnabled();
}
