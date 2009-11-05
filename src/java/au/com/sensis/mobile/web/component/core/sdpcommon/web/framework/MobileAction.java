package au.com.sensis.mobile.web.component.core.sdpcommon.web.framework;



/**
 * The interface that defines an action in a Mobiles application.
 *
 * @param <T> the {@link MobileContext} type.
 *
 * @author Boyd Sharrock (cloned from WPM).
 */
public interface MobileAction<T extends MobileContext> {

    /**
     * @return  the {@link MobileContext} type.
     */
    T getContext();

    /**
     * @param context   the {@link MobileContext} type.
     */
    void setContext(T context);
}
