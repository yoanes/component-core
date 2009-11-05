package au.com.sensis.mobile.web.component.core.sdpcommon.web.framework;



/**
 * Provides the concrete class for the standard mobile context set up.
 *
 * See {@link AbstractMobileContextSetUp}.
 *
 * @author Boyd Sharrock (cloned from WPM).
 */
@SuppressWarnings("serial")
public class MobileContextSetUp
        extends AbstractMobileContextSetUp<MobileContext> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected MobileContext newBusinessContext() {

        return new MobileContext();
    }
}
