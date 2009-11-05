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

import au.com.sensis.wireless.web.filter.McsVFormRemovalRequestWrapper;

/**
 * This is a {@link Filter} implementation that works around the problem of MCS throwing an
 * exception when a session times out.
 *
 * @author Jeff Tan-Ang (cloned from TPM)
 */
public class McsVFormRemovalRequestWrapperFilter
        implements Filter {

    private static final Logger LOGGER
            = Logger.getLogger(McsVFormRemovalRequestWrapperFilter.class);

    /**
     * Initialises this servlet {@link Filter}. This method currently does nothing.
     *
     * @param config    the {@link FilterConfig} object.
     *
     * @throws ServletException if there was a problem initialising this {@link Filter}.
     *
     * @see Filter#init(FilterConfig)
     */
    public void init(final FilterConfig config)
            throws ServletException {

        // Does nothing.
    }

    /**
     * Destroys this servlet {@link Filter}. This method currently does nothing.
     *
     * @see Filter#init(FilterConfig)
     */
    public void destroy() {

        // Does nothing.
    }

    /**
     * Wraps the original request with the {@link McsVFormRemovalRequestWrapper} wrapper object,
     * which removes the vform parameter.
     *
     * @param servletRequest    the {@link ServletRequest} object.
     * @param servletResponse   the {@link ServletResponse} object.
     * @param chain             the {@link FilterChain} object.
     *
     * @throws IOException      for IO related exceptions.
     * @throws ServletException for all other exceptions.
     */
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
            final FilterChain chain)
            throws IOException, ServletException {

        // Wrap the original request with the vform removal wrapper.
        LOGGER.debug("Wrapping the original request with McsVFormRemovalRequestWrapper.");

        final HttpServletRequest request =
                new McsVFormRemovalRequestWrapper((HttpServletRequest) servletRequest);
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Pass down the chain.
        chain.doFilter(request, response);
    }
}
