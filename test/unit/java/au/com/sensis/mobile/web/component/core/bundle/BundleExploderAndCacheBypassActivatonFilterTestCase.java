package au.com.sensis.mobile.web.component.core.bundle;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link BundleExploderActivatonFilter}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class BundleExploderAndCacheBypassActivatonFilterTestCase extends
        AbstractJUnit4TestCase {

    private BundleExploderActivatonFilter objectUnderTest;

    private ServletRequest mockServletRequest;
    private ServletResponse mockServletResponse;
    private HttpServletRequest mockHttpServletRequest;
    private HttpServletResponse mockHttpServletResponse;
    private MockHttpServletRequest springMockHttpServletRequest;
    private MockHttpServletResponse springMockHttpServletResponse;
    private FilterChain mockFilterChain;
    private MockHttpSession mockHttpSession;
    private SimpleFeatureEnablementRegistryBean simpleFeatureEnablementRegistryBean;
    private FilterConfig mockFilterConfig;

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        objectUnderTest = new BundleExploderActivatonFilter();

        setSimpleFeatureEnablementRegistryBean(new SimpleFeatureEnablementRegistryBean());
        objectUnderTest.setFeatureEnablementRegistry(getSimpleFeatureEnablementRegistryBean());

        setSpringMockHttpServletRequest(new MockHttpServletRequest());
        setSpringMockHttpServletResponse(new MockHttpServletResponse());

        setMockHttpSession(new MockHttpSession());

        getSpringMockHttpServletRequest().setSession(getMockHttpSession());
    }

    @Test
    public void testDoFilterWhenServletRequestIsNotHttpServletRequest()
            throws Throwable {
        try {
            objectUnderTest.doFilter(getMockServletRequest(),
                    getMockHttpServletResponse(), getMockFilterChain());
            Assert.fail("IllegalArgumentException expected.");
        } catch (final IllegalArgumentException e) {
            Assert.assertEquals("IllegalArgumentException has wrong message",
                    "Request must be an instance of HttpServletRequest but was: "
                            + getMockServletRequest(), e
                            .getMessage());
        }
    }

    @Test
    public void testDoFilterWhenServletResponseIsNotHttpServletResponse()
    throws Throwable {
        try {
            objectUnderTest.doFilter(getMockHttpServletRequest(),
                    getMockServletResponse(), getMockFilterChain());
            Assert.fail("IllegalArgumentException expected.");
        } catch (final IllegalArgumentException e) {
            Assert.assertEquals("IllegalArgumentException has wrong message",
                    "Response must be an instance of HttpServletResponse but was: "
                    + getMockServletResponse(), e
                    .getMessage());
        }
    }

    @Test
    public void testInit()
        throws Throwable {

        replay();

        objectUnderTest.init(getMockFilterConfig());

        // Nothing to assert.
    }

    @Test
    public void testDestroy()
        throws Throwable {

        replay();

        objectUnderTest.destroy();

        // Nothing to assert.
    }

    @Test
    public void testDoFilterWhenBundleExploderRequestIsNullAndFeatureEnabled()
        throws Throwable {
        enableBundleExplosionFeature();

        getMockFilterChain().doFilter(
                getSpringMockHttpServletRequest(), getSpringMockHttpServletResponse());

        replay();

        objectUnderTest.doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getMockFilterChain());

        assertBundleExploderSessionAttributeValue(null);
    }

    private void assertBundleExploderSessionAttributeValue(final Object expectedAttributeValue) {
        Assert.assertEquals("bundle exploder session attribute is wrong", expectedAttributeValue,
                getSpringMockHttpServletRequest().getSession().getAttribute(
                        ResourceBundleLoaderController.BUNDLE_EXPLODER_REQUEST_SESSION_KEY));
    }

    @Test
    public void testDoFilterWhenBundleExploderRequestIsTrueAndFeatureEnabled()
        throws Throwable {
        enableBundleExplosionFeature();

        recordBundleExploderRequestParamTrue();

        getMockFilterChain().doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse());

        replay();

        objectUnderTest.doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getMockFilterChain());

        assertBundleExploderSessionAttributeValue(Boolean.TRUE);
    }

    @Test
    public void testDoFilterWhenBundleExploderRequestIsFalseAndFeatureEnabled()
            throws Throwable {
        enableBundleExplosionFeature();

        recordBundleExploderRequestParamFalse();

        getMockFilterChain().doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse());

        replay();

        objectUnderTest.doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getMockFilterChain());

        assertBundleExploderSessionAttributeValue(Boolean.FALSE);

    }

    @Test
    public void testDoFilterWhenBundleExploderRequestIsTrueButFeatureDisabled()
            throws Throwable {
        getSimpleFeatureEnablementRegistryBean().setBundleExplosionEnabled(
                false);

        recordBundleExploderRequestParamTrue();

        getMockFilterChain().doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse());

        replay();

        objectUnderTest.doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getMockFilterChain());

        assertBundleExploderSessionAttributeValue(null);
    }

    @Test
    public void testDoFilterWhenBundleExploderRequestTrueDefaultButNoSession()
        throws Throwable {
        getSpringMockHttpServletRequest().setSession(null);

        getObjectUnderTest().setBundleExplosionInitialValue(true);

        enableBundleExplosionFeature();

        getMockFilterChain().doFilter(
                getSpringMockHttpServletRequest(), getSpringMockHttpServletResponse());

        replay();

        objectUnderTest.doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getMockFilterChain());

        assertBundleExploderSessionAttributeValue(Boolean.TRUE);
    }

    @Test
    public void testDoFilterWhenBundleExploderRequestTrueDefaultAndSessionExists()
        throws Throwable {

        getObjectUnderTest().setBundleExplosionInitialValue(true);

        enableBundleExplosionFeature();

        getMockFilterChain().doFilter(
                getSpringMockHttpServletRequest(), getSpringMockHttpServletResponse());

        replay();

        objectUnderTest.doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getMockFilterChain());

        assertBundleExploderSessionAttributeValue(null);
    }

    @Test
    public void testDoFilterWhenBundleExploderRequestTrueDefaultButDisabled()
        throws Throwable {
        getSpringMockHttpServletRequest().setSession(null);

        getObjectUnderTest().setBundleExplosionInitialValue(true);

        getMockFilterChain().doFilter(
                getSpringMockHttpServletRequest(), getSpringMockHttpServletResponse());

        replay();

        objectUnderTest.doFilter(getSpringMockHttpServletRequest(),
                getSpringMockHttpServletResponse(), getMockFilterChain());

        assertBundleExploderSessionAttributeValue(null);
    }

    private void recordBundleExploderRequestParamTrue() {
        getSpringMockHttpServletRequest()
                .setParameter(
                    BundleExploderActivatonFilter.BUNDLE_EXPLODER_REQUEST_PARAM_NAME,
                    BundleExploderActivatonFilter.BOOLEAN_TRUE_PARAM_VALUE);
    }

    private void recordBundleExploderRequestParamFalse() {
        getSpringMockHttpServletRequest()
            .setParameter(
                BundleExploderActivatonFilter.BUNDLE_EXPLODER_REQUEST_PARAM_NAME,
                "0");
    }

    private void enableBundleExplosionFeature() {
        getSimpleFeatureEnablementRegistryBean().setBundleExplosionEnabled(true);
    }

    /**
     * @return the mockServletRequest
     */
    public ServletRequest getMockServletRequest() {
        return mockServletRequest;
    }

    /**
     * @param mockServletRequest the mockServletRequest to set
     */
    public void setMockServletRequest(final ServletRequest mockServletRequest) {
        this.mockServletRequest = mockServletRequest;
    }

    /**
     * @return the mockServletResponse
     */
    public ServletResponse getMockServletResponse() {
        return mockServletResponse;
    }

    /**
     * @param mockServletResponse the mockServletResponse to set
     */
    public void setMockServletResponse(final ServletResponse mockServletResponse) {
        this.mockServletResponse = mockServletResponse;
    }

    /**
     * @return the mockHttpServletRequest
     */
    public HttpServletRequest getMockHttpServletRequest() {
        return mockHttpServletRequest;
    }

    /**
     * @param mockHttpServletRequest the mockHttpServletRequest to set
     */
    public void setMockHttpServletRequest(final HttpServletRequest mockHttpServletRequest) {
        this.mockHttpServletRequest = mockHttpServletRequest;
    }

    /**
     * @return the mockHttpServletResponse
     */
    public HttpServletResponse getMockHttpServletResponse() {
        return mockHttpServletResponse;
    }

    /**
     * @param mockHttpServletResponse the mockHttpServletResponse to set
     */
    public void setMockHttpServletResponse(
            final HttpServletResponse mockHttpServletResponse) {
        this.mockHttpServletResponse = mockHttpServletResponse;
    }

    /**
     * @return the objectUnderTest
     */
    public BundleExploderActivatonFilter getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @return the mockFilterChain
     */
    public FilterChain getMockFilterChain() {
        return mockFilterChain;
    }

    /**
     * @param mockFilterChain the mockFilterChain to set
     */
    public void setMockFilterChain(final FilterChain mockFilterChain) {
        this.mockFilterChain = mockFilterChain;
    }

    /**
     * @return the mockHttpSession
     */
    public MockHttpSession getMockHttpSession() {
        return mockHttpSession;
    }

    /**
     * @param mockHttpSession the mockHttpSession to set
     */
    public void setMockHttpSession(final MockHttpSession mockHttpSession) {
        this.mockHttpSession = mockHttpSession;
    }

    /**
     * @return the simpleFeatureEnablementRegistryBean
     */
    public SimpleFeatureEnablementRegistryBean getSimpleFeatureEnablementRegistryBean() {
        return simpleFeatureEnablementRegistryBean;
    }

    /**
     * @param simpleFeatureEnablementRegistryBean the simpleFeatureEnablementRegistryBean to set
     */
    public void setSimpleFeatureEnablementRegistryBean(
            final SimpleFeatureEnablementRegistryBean simpleFeatureEnablementRegistryBean) {
        this.simpleFeatureEnablementRegistryBean =
                simpleFeatureEnablementRegistryBean;
    }

    /**
     * @return the springMockHttpServletRequest
     */
    public MockHttpServletRequest getSpringMockHttpServletRequest() {
        return springMockHttpServletRequest;
    }

    /**
     * @param springMockHttpServletRequest the springMockHttpServletRequest to set
     */
    public void setSpringMockHttpServletRequest(
            final MockHttpServletRequest springMockHttpServletRequest) {
        this.springMockHttpServletRequest = springMockHttpServletRequest;
    }

    /**
     * @return the springMockHttpServletResponse
     */
    public MockHttpServletResponse getSpringMockHttpServletResponse() {
        return springMockHttpServletResponse;
    }

    /**
     * @param springMockHttpServletResponse the springMockHttpServletResponse to set
     */
    public void setSpringMockHttpServletResponse(
            final MockHttpServletResponse springMockHttpServletResponse) {
        this.springMockHttpServletResponse = springMockHttpServletResponse;
    }

    /**
     * @return the mockFilterConfig
     */
    public FilterConfig getMockFilterConfig() {
        return mockFilterConfig;
    }

    /**
     * @param mockFilterConfig the mockFilterConfig to set
     */
    public void setMockFilterConfig(final FilterConfig mockFilterConfig) {
        this.mockFilterConfig = mockFilterConfig;
    }

}
