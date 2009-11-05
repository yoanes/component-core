package au.com.sensis.mobile.web.component.core.sdpcommon.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * A ServletFilter that can capture & log the request / response.
 *
 * @author Boyd Sharrock (cloned from TPM)
 */
public class ServletCaptureFilter
        implements Filter {

    private static final Logger LOGGER = Logger.getLogger(ServletCaptureFilter.class);
    private static final Logger CAPTURE_LOGGER = Logger.getLogger("SERVLET_CAPTURE");

    /**
     * Nothing to do.
     */
    public void destroy() {

        // Nothing to do...
    }

    /**
     * Wraps the the response so that we can capture the output.
     *
     * @param request   the {@link ServletRequest}.
     * @param response  the {@link ServletResponse}.
     * @param chain     the {@link FilterChain}.
     *
     * @throws IOException      .
     * @throws ServletException .
     */
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {

        if (! captureNeeded((HttpServletRequest) request)) {

            // Pass the request on unaltered...
            chain.doFilter(request, response);
            return;
        }

        LOGGER.info("Capturing...");

        logRequestInformation((HttpServletRequest) request);

        final ServletResponseCaptureWrapper responseWrapper
                = createServletResponseCaptureWrapper(response);

        chain.doFilter(request, responseWrapper);

        logResponseInformation(responseWrapper);

        // Ensure response is written to the real stream...
        response.getWriter().print(responseWrapper.getCapturedOutput());

        LOGGER.info("Capturing... OK");
    }

    /**
     * Nothing to do.
     *
     * @param config    unused.
     *
     * @throws ServletException never.
     */
    public void init(final FilterConfig config)
            throws ServletException {

        // nothing to do.
    }

    /**
     * Should we capture the servlet input / output?
     *
     * We do it when the logger is set to DEBUG or there is a header "SERVLET_CAPTURE" == "ON".
     *
     * @param request   the {@link HttpServletRequest}.
     *
     * @return  true if we should.
     */
    protected boolean captureNeeded(final HttpServletRequest request) {

        return getLogger().isDebugEnabled() || "ON".equals(request.getHeader("SERVLET_CAPTURE"));
    }

    /**
     * Log information about the request before we pass on the request to the next item in the
     * filter.
     *
     * @param request   the {@link HttpServletRequest}.
     */
    protected void logRequestInformation(final HttpServletRequest request) {

        final String requestUrl = request.getRequestURI();
        final String queryParams = request.getQueryString();

        getLogger().info(">>>>> REQUEST ---------------------------\n\n" + requestUrl
                + (queryParams != null ? "?" + queryParams : ""));
    }

    /**
     * Logs the raw servlet response as captured by the {@link ServletResponseCaptureWrapper}.
     *
     * @param response  the wrapped response capturer.
     */
    protected void logResponseInformation(final ServletResponseCaptureWrapper response) {

        // Capture the output, remove the extra carriage returns to make it more compact / readable.

        getLogger().info("<<<<< RESPONSE ---------------------------"
                + response.getCapturedOutput().replaceAll("\\r\\n(\\r\\n)+", "\r\n")
                + "\n\n<<<<< END ---------------------------");
    }

    /**
     * Separate out the object construction.
     *
     * @param response  a {@link ServletResponse}.
     *
     * @return  a {@link ServletResponseCaptureWrapper}.
     */
    protected ServletResponseCaptureWrapper createServletResponseCaptureWrapper(
            final ServletResponse response) {

        return new ServletResponseCaptureWrapper((HttpServletResponse) response);
    }

    /**
     * Access to Logger via a protected method to allow for easy testing.
     *
     * @return  a {@link Logger}.
     */
    protected Logger getLogger() {

        return CAPTURE_LOGGER;
    }
}
