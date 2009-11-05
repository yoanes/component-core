package au.com.sensis.mobile.web.component.core.sdpcommon.web;

import au.com.sensis.sal.common.EntryPoint;
import au.com.sensis.sal.common.Provider;

/**
 * Provides contextual information about the site visitor as inferred from the Headers.
 *
 * @author Boyd Sharrock
 */
public class Visitor {

    private String userId;
    private EntryPoint entryPoint = EntryPoint.MOBILE;
    private Provider provider = Provider.UNKNOWN;
    private String ipAddress;
    private String telstraSessId;
    private String userAgent;
    private String hostName;
    private String httpSessionId;
    private String carrierUserId;

    /**
     * @return  the userId.
     */
    public String getUserId() {

        return userId;
    }

    /**
     * @param userId    the userId.
     */
    public void setUserId(final String userId) {

        this.userId = userId;
    }


    /**
     * @return  the entryPoint.
     */
    public EntryPoint getEntryPoint() {

        return entryPoint;
    }

    /**
     * @param entryPoint    the entryPoint to set.
     */
    public void setEntryPoint(final EntryPoint entryPoint) {

        this.entryPoint = entryPoint;
    }

    /**
     * @return  the provider.
     */
    public Provider getProvider() {

        return provider;
    }

    /**
     * @param provider  the provider to set.
     */
    public void setProvider(final Provider provider) {

        this.provider = provider;
    }

    /**
     * @return  the ipAddress.
     */
    public String getIpAddress() {

        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set.
     */

    public void setIpAddress(final String ipAddress) {

        this.ipAddress = ipAddress;
    }

    /**
     * @return  the telstraSessId.
     */
    public String getTelstraSessId() {

        return telstraSessId;
    }

    /**
     * @param telstraSessId the telstraSessId to set.
     */
    public void setTelstraSessId(final String telstraSessId) {

        this.telstraSessId = telstraSessId;
    }

    /**
     * @return  the userAgent.
     */
    public String getUserAgent() {

        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set.
     */
    public void setUserAgent(final String userAgent) {

        this.userAgent = userAgent;
    }

    /**
     * @return  true if this is a Telstra customer.
     */
    public boolean isTelstraCustomer() {

        return Provider.TELSTRA.equals(provider);
    }

    /**
     * @return  true if this is a Three customer.
     */
    public boolean isThreeCustomer() {

        return Provider.THREE.equals(provider);
    }

    /**
     * @return  true if this customer qualifies for Free To Browse.
     */
    public boolean isFreeToBrowse() {

        return isTelstraCustomer() && EntryPoint.MOBILE.equals(getEntryPoint());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder("Visitor");

        builder.append(" [ Provider - " + provider + "]");
        builder.append(" [ EntryPoint - " + entryPoint + "]");

        return builder.toString();
    }

    /**
     * @return Our hostname.
     */
    public String getHostName() {
        return hostName;
    }


    /**
     * @param hostName the hostName to set.
     */
    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the user's web session id (i.e. jsessionid).
     */
    public String getHttpSessionId() {

        return httpSessionId;
    }


    /**
     * @param httpSessionId the httpSessionId to set (i.e. jsessionid).
     */
    public void setHttpSessionId(final String httpSessionId) {

        this.httpSessionId = httpSessionId;
    }

    /**
     * @return  the carrierUserId (i.e. the MSISDN).
     */
    public String getCarrierUserId() {

        return carrierUserId;
    }

    /**
     * @param carrierUserId the carrierUserId to set (i.e. the MSISDN).
     */
    public void setCarrierUserId(final String carrierUserId) {

        this.carrierUserId = carrierUserId;
    }
}

