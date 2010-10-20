package au.com.sensis.mobile.web.component.core.util;

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
 * Simple filter for trimming leading and trailing whitespace from JSON responses.
 * Will only wrap the response if the request was an AJAX request and will only
 * trim such a response if the response has a JavaScript content type.
 * Note that this filter does not validate that the actual content is JSON. The name
 * of the class really just reflects the usage intent.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class JsonLeadingAndTrailingWhitespaceTrimmingFilter implements Filter {

    private static final Logger LOGGER
        = Logger.getLogger(JsonLeadingAndTrailingWhitespaceTrimmingFilter.class);

    private static final int DEFAULT_CHAR_ARRAY_WRITER_SIZE = 1000;
    private int initialCharArrayWriterSize = DEFAULT_CHAR_ARRAY_WRITER_SIZE;

    private XmlHttpRequestDetector xmlHttpRequestDetector;

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        // Do nothing.
    }

    /**
     * Trimm leading and trailing whitespace from JSON responses. Will only
     * wrap the response if the request was an AJAX request and will only trim
     * such a response if the response has a JavaScript content type.
     *
     * @param servletRequest {@link ServletRequest} passed in.
     * @param servletResponse {@link ServletResponse} passed in.
     * @param filterChain {@link FilterChain} to pass the request/response along to.
     * @throws IOException Thrown if an IOException occurs.
     * @throws ServletException Thrown if any other error occurs.
     */
    public void doFilter(final ServletRequest servletRequest,
            final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {

        if (shouldWrapResponse(servletRequest)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Will wrap the response for this request.");
            }

            wrapResponseAndTrimIfNecessary(servletRequest, servletResponse, filterChain);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Will not wrap the response for this request.");
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void wrapResponseAndTrimIfNecessary(final ServletRequest servletRequest,
            final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletResponse httpServletResponse =
                (HttpServletResponse) servletResponse;
        final CharArrayWriterHttpServletResponseWrapper wrapper =
                new CharArrayWriterHttpServletResponseWrapper(
                        httpServletResponse,
                        getInitialCharArrayWriterSize());

        filterChain.doFilter(servletRequest, wrapper);

        if (shouldTrimResponse(wrapper)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Will trim the response for this request.");
            }

            httpServletResponse.getWriter().write(
                    wrapper.getWriterAsString().trim());
            httpServletResponse.getWriter().close();
        } else {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Will not trim the response for this request.");
            }

            httpServletResponse.getWriter().write(
                    wrapper.getWriterAsString().toString());
            httpServletResponse.getWriter().close();
        }
    }

    private boolean shouldTrimResponse(
            final CharArrayWriterHttpServletResponseWrapper wrapper) {
        return "text/javascript".equalsIgnoreCase(wrapper.getContentType());
    }

    private boolean shouldWrapResponse(final ServletRequest servletRequest) {

        final HttpServletRequest httpServletRequest =
            (HttpServletRequest) servletRequest;

        return getXmlHttpRequestDetector().isXmlHttpRequest(httpServletRequest);
    }

    /**
     * {@inheritDoc}
     */
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Do nothing.
    }

    /**
     * @return the initialCharArrayWriterSize
     */
    public int getInitialCharArrayWriterSize() {
        return initialCharArrayWriterSize;
    }

    /**
     * @param initialCharArrayWriterSize the initialCharArrayWriterSize to set
     */
    public void setInitialCharArrayWriterSize(final int initialCharArrayWriterSize) {
        this.initialCharArrayWriterSize = initialCharArrayWriterSize;
    }

    /**
     * @return the xmlHttpRequestDetector
     */
    public XmlHttpRequestDetector getXmlHttpRequestDetector() {
        return xmlHttpRequestDetector;
    }

    /**
     * @param xmlHttpRequestDetector the xmlHttpRequestDetector to set
     */
    public void setXmlHttpRequestDetector(
            final XmlHttpRequestDetector xmlHttpRequestDetector) {
        this.xmlHttpRequestDetector = xmlHttpRequestDetector;
    }
}
