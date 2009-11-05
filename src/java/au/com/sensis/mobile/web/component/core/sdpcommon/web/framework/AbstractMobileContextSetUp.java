package au.com.sensis.mobile.web.component.core.sdpcommon.web.framework;

import org.apache.log4j.Logger;

import au.com.sensis.mobile.web.component.core.sdpcommon.web.Device;
import au.com.sensis.mobile.web.component.core.sdpcommon.web.DeviceRecognition;
import au.com.sensis.mobile.web.component.core.sdpcommon.web.HeaderInterpreter;
import au.com.sensis.mobile.web.component.core.sdpcommon.web.HttpHeaderConstants;
import au.com.sensis.mobile.web.component.core.sdpcommon.web.Visitor;
import au.com.sensis.mobile.web.component.core.sdpcommon.web.struts2.AbstractInterceptor;
import au.com.sensis.wireless.web.common.exception.ApplicationRuntimeException;
import au.com.sensis.wireless.web.common.validation.Validatable;
import au.com.sensis.wireless.web.common.validation.ValidatableUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * A Struts2 Interceptor that sets up the user context information.
 *
 * This is...
 *
 * - Access to information about the "visitor" of the site (based on the headers).
 * - Access to information about the device that is accessing site.
 *
 * @param <C> the MobileContext type.
 *
 * @author Boyd Sharrock
 */
@SuppressWarnings("serial")
public abstract class AbstractMobileContextSetUp<C extends MobileContext>
        extends AbstractInterceptor implements Interceptor, Validatable {

    private static final Logger LOGGER = Logger.getLogger(AbstractMobileContextSetUp.class);

    private DeviceRecognition deviceRecognition;

    private HeaderInterpreter headerInterpreter;

    /**
     * Set up the "Business Context" before the action itself is invoked.
     *
     * The BusinessContext supplies information about the user that is making the request.
     *
     * @param invocation    the {@link ActionInvocation} object.
     *
     * @return  the action result name.
     *
     * @throws Exception    if there was a problem in the interception.
     */
    @SuppressWarnings("unchecked")
    @Override
    public String intercept(final ActionInvocation invocation)
            throws Exception {

        LOGGER.debug("Setting up Business Context...");

        final MobileAction<MobileContext> action
                = (MobileAction<MobileContext>) invocation.getAction();

        if (action.getContext() == null) {

            final C context = newBusinessContext();

            loadContext(context, invocation);

            action.setContext(context);

        } else {

            LOGGER.debug("Business Context Exists!");
        }

        LOGGER.debug("Setting up Business Context... OK");

        return invocation.invoke();
    }

    /**
     * Loads the given context with properties inferred from the {@link ActionInvocation}.
     *
     * @param context       the {@link MobileContext} type.
     * @param invocation    the {@link ActionInvocation} object.
     */
    protected void loadContext(final C context, final ActionInvocation invocation) {

        resolveDevice(context, invocation);
        interpretHeaders(context, invocation);
        accessSession(context, invocation);
    }

    /**
     * Resolves a {@link Device} from the given context and {@link ActionInvocation}.
     *
     * @param context       the {@link MobileContext} type.
     * @param invocation    the {@link ActionInvocation}.
     *
     * @return  an identified {@link Device}.
     */
    protected Device resolveDevice(final C context, final ActionInvocation invocation) {

        final Device device = getDeviceRecognition().identifyDevice(
                getRequest(invocation).getHeader(HttpHeaderConstants.USER_AGENT));

        context.setDevice(device);

        if (LOGGER.isDebugEnabled()) {

            LOGGER.debug("resolveDevice - " + device);
        }

        return device;
    }

    private Visitor interpretHeaders(final C context, final ActionInvocation invocation) {

        final Visitor visitor = getHeaderInterpreter().interpretHeaders(getRequest(invocation));

        context.setVisitor(visitor);

        if (LOGGER.isDebugEnabled()) {

            LOGGER.debug("interpretHeaders - " + visitor);
        }

        return visitor;
    }

    @SuppressWarnings("unchecked")
    private void accessSession(final C context, final ActionInvocation invocation) {

        context.setRawSessionData(invocation.getInvocationContext().getSession());
    }

    /**
     * To be implemented to return a new {@link MobileContext} type object.
     *
     * @return  a new {@link MobileContext} type object.
     */
    protected abstract C newBusinessContext();

    /**
     * {@inheritDoc}
     */
    public void validateState()
            throws ApplicationRuntimeException {

        ValidatableUtils.validateObjectIsNotNull(getDeviceRecognition(), "deviceRecognition");
        ValidatableUtils.validateObjectIsNotNull(getHeaderInterpreter(), "headerInterpreter");
    }

    /**
     * @return  the deviceRecognition.
     */
    public DeviceRecognition getDeviceRecognition() {

        return deviceRecognition;
    }

    /**
     * @param deviceRecognition the deviceRecognition to set.
     */
    public void setDeviceRecognition(final DeviceRecognition deviceRecognition) {

        this.deviceRecognition = deviceRecognition;
    }

    /**
     * @return  the headerInterpreter.
     */
    public HeaderInterpreter getHeaderInterpreter() {

        return headerInterpreter;
    }

    /**
     * @param headerInterpreter the headerInterpreter to set.
     */
    public void setHeaderInterpreter(final HeaderInterpreter headerInterpreter) {

        this.headerInterpreter = headerInterpreter;
    }
}
