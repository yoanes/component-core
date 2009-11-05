package au.com.sensis.mobile.web.component.core.sdpcommon.web.framework;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.com.sensis.mobile.web.component.core.sdpcommon.web.Device;
import au.com.sensis.mobile.web.component.core.sdpcommon.web.Visitor;
import au.com.sensis.sal.common.UserContext;
import au.com.sensis.wireless.web.common.AgentDetails;

/**
 * Provides information about the user to support the processing of a request.
 *
 * See {@link MobileContextSetUp}.
 *
 * @author Boyd Sharrock
 */
public class MobileContext {

    private final Date creationDate = new Date();

    private Device device;

    private Visitor visitor;

    /** This is the session data - abstracted as a Map. */
    protected Map<String, Object> rawSessionData = new HashMap<String, Object>();

    /**
     * Adapts this context object to be a UserContext, as used by several SAL & sdp common services.
     *
     * @return  a {@link UserContext}.
     */
    public UserContext asUserContext() {

        final UserContext userContext = new UserContext();

        userContext.setEntryPoint(getVisitor().getEntryPoint());
        userContext.setProvider(getVisitor().getProvider());
        userContext.setUserId(getVisitor().getUserId());
        userContext.setIpAddress(getVisitor().getIpAddress());
        userContext.setCarrierUserId(getVisitor().getCarrierUserId());

        return userContext;
    }

    /**
     * Adapts this context object to be a {@link AgentDetails}, as used by several SAL & sdp common
     * services.
     *
     * @return a {@link AgentDetails}.
     */
    public AgentDetails asAgentDetails() {

        final AgentDetails agentDetails = new AgentDetails();

        agentDetails.setCallFwdSupport(getDevice().getCallForward());
        agentDetails.setCookiesEnabled(String.valueOf(getDevice().isCookiesEnabled()));
        agentDetails.setDeviceName(getDevice().getName());
        agentDetails.setJavascriptVersion(getDevice().getJavaScriptVersion());
        agentDetails.setPositioningType(getDevice().getPositioningType());
        agentDetails.setResultsPerPage(String.valueOf(getDevice().getResultsPerPage()));
        agentDetails.setScreenHeight(String.valueOf(getDevice().getPixelsY()));
        agentDetails.setScreenWidth(String.valueOf(getDevice().getPixelsX()));
        agentDetails.setUserAgent(getVisitor().getUserAgent());

        return agentDetails;
    }

    /**
     * @return  the creationDate.
     */
    public Date getCreationDate() {

        return creationDate;
    }

    /**
     * @return  the device.
     */
    public Device getDevice() {

        return device;
    }

    /**
     * @param device    the device to set.
     */
    protected void setDevice(final Device device) {

        this.device = device;
    }

    /**
     * @return  the visitor.
     */
    public Visitor getVisitor() {

        return visitor;
    }

    /**
     * @param visitor   the visitor to set.
     */
    protected void setVisitor(final Visitor visitor) {

        this.visitor = visitor;
    }

    /**
     * @return  the rawSessionData.
     */
    protected Map<String, Object> getRawSessionData() {

        return rawSessionData;
    }

    /**
     * @param rawSessionData    the rawSessionData to set.
     */
    protected void setRawSessionData(final Map<String, Object> rawSessionData) {

        this.rawSessionData = rawSessionData;
    }
}
