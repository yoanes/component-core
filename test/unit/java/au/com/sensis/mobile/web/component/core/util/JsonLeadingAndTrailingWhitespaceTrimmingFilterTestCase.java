package au.com.sensis.mobile.web.component.core.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link JsonLeadingAndTrailingWhitespaceTrimmingFilter}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class JsonLeadingAndTrailingWhitespaceTrimmingFilterTestCase extends
        AbstractJUnit4TestCase {

    private JsonLeadingAndTrailingWhitespaceTrimmingFilter objectUnderTest;
    private MockHttpServletRequest springMockHttpServletRequest;
    private MockHttpServletResponse springMockHttpServletResponse;
    private TestFilterChain testFilterChain;
    private XmlHttpRequestDetector mockXmlHttpRequestDetector;

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new JsonLeadingAndTrailingWhitespaceTrimmingFilter());

        getObjectUnderTest().setXmlHttpRequestDetector(getMockXmlHttpRequestDetector());

        setSpringMockHttpServletRequest(new MockHttpServletRequest());
        setSpringMockHttpServletResponse(new MockHttpServletResponse());
    }

    @Test
    public void testDoFilterWhenShouldNotWrapResponse() throws Throwable {

        final String[] testResponseContentTypes =
                { "text/javascript", "application/javascript", "text/html" };
        for (final String responseContentType : testResponseContentTypes) {
            setTestFilterChain(new TestFilterChain(responseContentType));

            recordIsNotAjaxRequest();

            replay();

            getObjectUnderTest().doFilter(getSpringMockHttpServletRequest(),
                    getSpringMockHttpServletResponse(), getTestFilterChain());

            Assert.assertEquals("response content is wrong",
                    TestFilterChain.UNSTRIPPED_RESPONSE_CONTENT,
                    getSpringMockHttpServletResponse().getContentAsString());

            verify();

            // Reset mocks prior to next iteration.
            setReplayed(false);
            reset();
        }

    }

    @Test
    public void testDoFilterWhenShouldWrapAndTrimResponse() throws Throwable {

        final String[] testResponseContentTypes =
                { "text/javascript", "application/javascript" };
        for (final String responseContentType : testResponseContentTypes) {
            setTestFilterChain(new TestFilterChain(responseContentType));

            recordIsAjaxRequest();

            replay();

            getObjectUnderTest().doFilter(getSpringMockHttpServletRequest(),
                    getSpringMockHttpServletResponse(), getTestFilterChain());

            Assert.assertEquals("response content is wrong",
                    TestFilterChain.STRIPPED_RESPONSE_CONTENT,
                    getSpringMockHttpServletResponse().getContentAsString());

            verify();

            // Reset mocks prior to next iteration.
            setReplayed(false);
            reset();
        }
    }

    @Test
    public void testDoFilterWhenShouldWrapButNotTrimResponse() throws Throwable {
        recordIsAjaxRequest();

        setTestFilterChain(new TestFilterChain("text/html"));

        replay();

        getObjectUnderTest().doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getTestFilterChain());

        Assert.assertEquals("response content is wrong",
                TestFilterChain.UNSTRIPPED_RESPONSE_CONTENT,
                getSpringMockHttpServletResponse().getContentAsString());

        verify();
    }

    private void recordIsNotAjaxRequest() {
        EasyMock.expect(
                getMockXmlHttpRequestDetector().isXmlHttpRequest(
                        getSpringMockHttpServletRequest())).andReturn(
                Boolean.FALSE);
    }

    private void recordIsAjaxRequest() {
        EasyMock.expect(
                getMockXmlHttpRequestDetector().isXmlHttpRequest(
                        getSpringMockHttpServletRequest())).andReturn(
                                Boolean.TRUE);
    }

    /**
     * @return the objectUnderTest
     */
    public JsonLeadingAndTrailingWhitespaceTrimmingFilter getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    private void setObjectUnderTest(
            final JsonLeadingAndTrailingWhitespaceTrimmingFilter objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the springMockHttpServletRequest
     */
    private MockHttpServletRequest getSpringMockHttpServletRequest() {
        return springMockHttpServletRequest;
    }

    /**
     * @param springMockHttpServletRequest the springMockHttpServletRequest to set
     */
    private void setSpringMockHttpServletRequest(
            final MockHttpServletRequest springMockHttpServletRequest) {
        this.springMockHttpServletRequest = springMockHttpServletRequest;
    }

    /**
     * @return the springMockHttpServletResponse
     */
    private MockHttpServletResponse getSpringMockHttpServletResponse() {
        return springMockHttpServletResponse;
    }

    /**
     * @param springMockHttpServletResponse the springMockHttpServletResponse to set
     */
    private void setSpringMockHttpServletResponse(
            final MockHttpServletResponse springMockHttpServletResponse) {
        this.springMockHttpServletResponse = springMockHttpServletResponse;
    }

    /**
     * Test {@link FilterChain} implementation.
     */
    public static class TestFilterChain implements FilterChain {

        /**
         * Arbitrary number of whitespaces.
         */
        public static final String WHITESPACES = StringUtils.repeat(" ", 10);

        /**
         * Response content stripped of whitespaces.
         */
        public static final String STRIPPED_RESPONSE_CONTENT = "my test response";

        /**
         * Response content with leading and trailing whitespaces.
         */
        public static final String UNSTRIPPED_RESPONSE_CONTENT =
                WHITESPACES + STRIPPED_RESPONSE_CONTENT + WHITESPACES;

        private final String responseContentType;

        public TestFilterChain(final String responseContentType) {
            this.responseContentType = responseContentType;
        }

        /**
         * Writes content to the response containing leading and trailing
         * whitespaces.
         *
         * @param servletRequest
         *            {@link ServletRequest}.
         * @param servletResponse
         *            {@link ServletResponse}.
         * @throws IOException
         *             Thrown if an IOException occurs.
         * @throws ServletException
         *             Thrown if any other error occurs.
         */
        public void doFilter(final ServletRequest servletRequest,
                final ServletResponse servletResponse) throws IOException,
                ServletException {
            servletResponse.setContentType(getResponseContentType());
            servletResponse.getWriter().write(
                    UNSTRIPPED_RESPONSE_CONTENT);
            servletResponse.getWriter().close();
        }

        /**
         * @return the responseContentType
         */
        private String getResponseContentType() {
            return responseContentType;
        }
    }

    /**
     * @return the mockXmlHttpRequestDetector
     */
    public XmlHttpRequestDetector getMockXmlHttpRequestDetector() {
        return mockXmlHttpRequestDetector;
    }

    /**
     * @return the mockXmlHttpRequestDetector
     */
    public void setMockXmlHttpRequestDetector(final XmlHttpRequestDetector xmlHttpRequestDetector) {
        mockXmlHttpRequestDetector = xmlHttpRequestDetector;
    }

    /**
     * @return the testFilterChain
     */
    private TestFilterChain getTestFilterChain() {
        return testFilterChain;
    }

    /**
     * @param testFilterChain the testFilterChain to set
     */
    private void setTestFilterChain(final TestFilterChain testFilterChain) {
        this.testFilterChain = testFilterChain;
    }
}
