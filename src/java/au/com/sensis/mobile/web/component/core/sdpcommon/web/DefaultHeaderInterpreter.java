package au.com.sensis.mobile.web.component.core.sdpcommon.web;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import au.com.sensis.sal.common.EntryPoint;
import au.com.sensis.sal.common.Provider;
import au.com.sensis.wireless.web.common.exception.ApplicationRuntimeException;
import au.com.sensis.wireless.web.common.validation.Validatable;

/**
 * Provides the default behaviour for a sensis mobile application when interpreting headers.
 *
 * @author Boyd Sharrock (cloned from TPM)
 */
public class DefaultHeaderInterpreter
        implements HeaderInterpreter, Validatable {

    private static final Logger LOGGER = Logger.getLogger(DefaultHeaderInterpreter.class);

    private static final String TELSTRA_UID_HEADER_NAME = "x-up-telstra-uid";

    private static final String X_HOST_HEADER_NAME = "x-host";

    private static final String PROVIDER_HEADER_NAME = "x-sensis-provider";

    private static final String CLIENT_IP_HEADER_NAME = "x-client-ip";

    private static final String TELSTRA_SESSID_HEADER_NAME = "x-up-telstra-loc-sessid";

    private static final String FORWARDED_IP_ADDRESSES_HEADER_NAME = "x-forwarded-for";

    private static final String USER_AGENT_HEADER_NAME = "user-agent";

    private static final String CARRIER_USER_ID = "x-up-calling-line-id";


    /**
     * Infers a {@link Visitor} from the {@link HttpServletRequest} headers.
     *
     * @param request   the {@link HttpServletRequest} object.
     *
     * @return  a populated {@link Visitor} object.
     */
    public Visitor interpretHeaders(final HttpServletRequest request) {

        final Visitor visitor = new Visitor();

        if (LOGGER.isDebugEnabled()) {

            LOGGER.debug(getHeaderLog(request));
        }

        populateVisitor(visitor, request);

        return visitor;
    }

    /**
     * {@inheritDoc}
     */
    public void validateState()
            throws ApplicationRuntimeException {

        // nothing to check.
    }

    /**
     * Gets a {@link String} representation of the current headers for logging purposes.
     *
     * @param httpServletRequest    the {@link HttpServletRequest}.
     *
     * @return  a {@link String} representation of the current headers for logging purposes.
     */
    @SuppressWarnings("unchecked")
    private String getHeaderLog(final HttpServletRequest httpServletRequest) {

        final String lineSeparator = System.getProperty("line.separator");

        final StringBuilder stringBuilder = new StringBuilder("---- Headers: ----");
        stringBuilder.append(lineSeparator);

        final Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        if (headerNames != null) {

            while (headerNames.hasMoreElements()) {

                final String headerName = headerNames.nextElement();

                stringBuilder.append("---- name: ");
                stringBuilder.append(headerName);
                stringBuilder.append(", value(s): ");

                int index = 0;
                final Enumeration<String> headerValues = httpServletRequest.getHeaders(headerName);
                if (headerValues != null) {

                    final String headerValue = headerValues.nextElement();

                    if (index > 0) {

                        stringBuilder.append(", ");
                    }

                    stringBuilder.append(headerValue);

                    index++;
                }

                stringBuilder.append(" ----");
                stringBuilder.append(lineSeparator);
            }
        }

        stringBuilder.append("---- Headers end ----");

        return stringBuilder.toString();
    }

    /**
     * Populates the given {@link Visitor} object with values from specific request headers.
     *
     * @param visitor   the {@link Visitor} object to populate.
     * @param request   the {@link HttpServletRequest} object for header values.
     */
    protected void populateVisitor(final Visitor visitor, final HttpServletRequest request) {

        Assert.notNull(visitor);

        interpretUID(request, visitor);
        interpretProvider(request, visitor);
        interpretEntryPoint(request, visitor);
        interpretIpAddress(request, visitor);
        interpretTelstraSessId(request, visitor);
        interpretUserAgent(request, visitor);
        interpretHost(request, visitor);
        interpretHttpSessionId(request, visitor);
    }

    private void interpretHost(final HttpServletRequest request, final Visitor visitor) {
        visitor.setHostName(request.getHeader(X_HOST_HEADER_NAME));
    }

    /**
     * Extracts the user-agent from the request header and populates
     * {@link Visitor#setUserAgent(String)}.
     *
     * @param request   the {@link HttpServletRequest} object.
     * @param visitor   the {@link Visitor} object.
     */
    protected void interpretUserAgent(final HttpServletRequest request, final Visitor visitor) {

        visitor.setUserAgent(request.getHeader(USER_AGENT_HEADER_NAME));
    }

    /**
     * Extracts the telstra UID if present and sets it into the visitor.
     *
     * @param request   the {@link HttpServletRequest}.
     * @param visitor   the {@link Visitor}.
     */
    protected void interpretUID(final HttpServletRequest request, final Visitor visitor) {

        visitor.setUserId(request.getHeader(TELSTRA_UID_HEADER_NAME));
    }

    /**
     * Looks at the headers to decide on which network (entry point) the user is coming from.
     *
     * @param request   the {@link HttpServletRequest}.
     * @param visitor   the {@link Visitor}.
     */
    protected void interpretEntryPoint(final HttpServletRequest request, final Visitor visitor) {

        final String host = request.getHeader(X_HOST_HEADER_NAME);

        if ((host != null) && (host.toLowerCase().startsWith("wap.")
                || host.toLowerCase().contains(".wap."))) {

            visitor.setEntryPoint(EntryPoint.WAP);

        } else {

            // We default to MOBILE...
            visitor.setEntryPoint(EntryPoint.MOBILE);
        }
    }

    /**
     * Detects the {@link Provider} from the given {@link HttpServletRequest} and {@link Visitor}.
     *
     * @param request   the {@link HttpServletRequest}.
     * @param visitor   the {@link Visitor}.
     */
    protected void interpretProvider(final HttpServletRequest request, final Visitor visitor) {

        final String providerFromHeader = request.getHeader(PROVIDER_HEADER_NAME);

        if (StringUtils.isNotBlank(providerFromHeader)) {

            try {

                visitor.setProvider(Provider.valueOf(providerFromHeader));

            } catch (final IllegalArgumentException e) {

                LOGGER.info("Unsupported provider - " + providerFromHeader);
                visitor.setProvider(Provider.UNKNOWN);
            }

        } else {

            visitor.setProvider(Provider.UNKNOWN);
        }
    }

    /**
     * Detects the Telstra session id from the given {@link HttpServletRequest} and {@link Visitor}
     * object.
     *
     * @param request   the {@link HttpServletRequest}.
     * @param visitor   the {@link Visitor}.
     */
    protected void interpretTelstraSessId(final HttpServletRequest request, final Visitor visitor) {

        visitor.setTelstraSessId(request.getHeader(TELSTRA_SESSID_HEADER_NAME));
    }

    /**
     * This method populates the {@link Visitor#setIpAddress(String)} with the most accurate IP
     * address available. This will first come from the first in the chain of IP addresses in the
     * "x-forwarded-for" request header values. Otherwise if none was found, then get directly from
     * the "x-client-ip" request header.
     *
     * @param request   the {@link HttpServletRequest}.
     * @param visitor   the {@link Visitor}.
     */
    protected void interpretIpAddress(final HttpServletRequest request, final Visitor visitor) {

        // If there was forwarded address, then take the first one in the chain.
        visitor.setIpAddress(getFirstTokenFromCsvHeader(request,
                FORWARDED_IP_ADDRESSES_HEADER_NAME));

        // If ip address is still blank, then get it from the client-ip header.
        if (StringUtils.isBlank(visitor.getIpAddress())) {

            visitor.setIpAddress(getFirstTokenFromCsvHeader(request, CLIENT_IP_HEADER_NAME));
        }

        // If ip address is still blank, then default to getRemoteAddr.
        if (StringUtils.isBlank(visitor.getIpAddress())) {

            visitor.setIpAddress(request.getRemoteAddr());
        }
    }

    /**
     * Retrieves the first token from the a CSV header value.
     *
     * @param request       the {@link HttpServletRequest}.
     * @param headerName    the header name.
     *
     * @return  the first token of the CSV header value.
     */
    @SuppressWarnings("unchecked")
    private String getFirstTokenFromCsvHeader(final HttpServletRequest request,
            final String headerName) {

        String firstToken = null;

        final Enumeration<String> headers = request.getHeaders(headerName);
        if ((headers != null) && headers.hasMoreElements()) {

            final String forwardedAddresses = headers.nextElement();
            if (StringUtils.isNotBlank(forwardedAddresses)) {

                final StrTokenizer tokeniser = StrTokenizer.getCSVInstance(forwardedAddresses);
                if (tokeniser.hasNext()) {

                    firstToken = tokeniser.nextToken();
                }
            }
        }

        return firstToken;
    }

    /**
     * Extracts the http session id from the request and populates {@link Visitor}.
     *
     * @param request   the {@link HttpServletRequest} object.
     * @param visitor   the {@link Visitor} object.
     */
    protected void interpretHttpSessionId(final HttpServletRequest request, final Visitor visitor) {

        visitor.setHttpSessionId(request.getSession().getId());
    }

    /**
     * Extracts the carrier user id (i.e. the users mobile number) from the request and populates
     * {@link Visitor}.
     * This had been added to the sdpcommon project after we started, and so we need to duplicate
     * the logic here.
     *
     * @param request   the {@link HttpServletRequest} object.
     * @param visitor   the {@link Visitor} object.
     */
    protected void interpretCarrierUserId(final HttpServletRequest request, final Visitor visitor) {

        visitor.setCarrierUserId(request.getHeader(CARRIER_USER_ID));
    }

}
