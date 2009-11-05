package au.com.sensis.mobile.web.component.core.sdpcommon.web.framework;

import java.util.Map;

/**
 * Base class which sets up the framework for type safe access to objects stored in the session.
 *
 * Individual projects should subclass this to add access to their specific objects.
 *
 * @author Boyd Sharrock (cloned from WPM)
 */
public class SensisMobileSession {

    private final Map<String, Object> sessionData;

    /**
     * Constructor.
     *
     * @param sessionData   the session data {@link Map}.
     */
    public SensisMobileSession(final Map<String, Object> sessionData) {

        this.sessionData = sessionData;
    }

    /**
     * Adds the given object to the session under the given key.
     *
     * @param key       to store the given object under
     * @param object    to store in the session
     */
    protected void store(final String key, final Object object) {

        getSessionData().put(key, object);
    }

    /**
     * Retrieves an Object from session for the given key.
     *
     * @param key   the key of the Object to be retrieved from session.
     *
     * @return  the {@link Object} corresponding to the given key.
     */
    protected Object retrieve(final String key) {

        return getSessionData().get(key);
    }

    /**
     * Removes the given key from session.
     *
     * @param key   the key of the entry to be removed from the session.
     */
    protected void reset(final String key) {

        getSessionData().remove(key);
    }

    /**
     * @return  the sessionData.
     */
    protected Map<String, Object> getSessionData() {

        return sessionData;
    }
}
