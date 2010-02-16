package au.com.sensis.mobile.web.component.core.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link XmlHttpRequestDetector}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class XmlHttpRequestDetectorTestCase extends AbstractJUnit4TestCase {

    private XmlHttpRequestDetector objectUnderTest;
    private MockHttpServletRequest springMockHttpServletRequest;

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new XmlHttpRequestDetector());
        setSpringMockHttpServletRequest(new MockHttpServletRequest());
    }

    @Test
    public void testIsXmlHttpRequest()
            throws Throwable {
        final TestData[] testData = TestData.createTestData();
        for (int i = 0; i < testData.length; i++) {

            if (testData[i].getXRequestedWithHeaderValue() != null) {
                getSpringMockHttpServletRequest().addHeader(
                        XmlHttpRequestDetector.X_REQUESTED_WITH_HEADER_NAME,
                        testData[i].getXRequestedWithHeaderValue());
                getSpringMockHttpServletRequest().setParameter(
                        XmlHttpRequestDetector.X_REQUESTED_WITH_PARAM_NAME,
                        testData[i].getXrwParamValue());
                Assert.assertEquals(
                        "isXmlHttpRequest() is wrong for testData index " + i,
                        testData[i].isXmlHttpRequest(), getObjectUnderTest()
                                .isXmlHttpRequest(
                                        getSpringMockHttpServletRequest()));

                /**
                 * Reset the Spring mock prior to the next iteration.
                 */
                setSpringMockHttpServletRequest(new MockHttpServletRequest());
            }
        }

    }

    /**
     * @return the objectUnderTest
     */
    public XmlHttpRequestDetector getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    public void setObjectUnderTest(final XmlHttpRequestDetector objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
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
     * Holder and creator of test data for this test case.
     */
    private static final class TestData {
        private final String xRequestedWithHeaderValue;
        private final String xrwParamValue;
        private final boolean xmlHttpRequest;

        private TestData(final String xRequestedWithHeaderValue,
                final String xrwParamValue, final boolean xmlHttpRequest) {
            this.xRequestedWithHeaderValue = xRequestedWithHeaderValue;
            this.xrwParamValue = xrwParamValue;
            this.xmlHttpRequest = xmlHttpRequest;
        }

        private static TestData [] createTestData() {
            return new TestData [] {
                    new TestData(null, null, false),

                    new TestData("XMLHttpRequest", null, true),
                    new TestData(null, "XHR", true),
                    new TestData("XMLHttpRequest", "XHR", true),

                    new TestData("xmlhttprequest", null, true),
                    new TestData(null, "xhr", true),
                    new TestData("xmlhttprequest", "xhr", true),

                    new TestData("XMLHttpRequested", null, false),
                    new TestData(null, "XHRd", false),
                    new TestData("XMLHttpRequested", "XHRd", false),

                    new TestData(StringUtils.EMPTY, null, false),
                    new TestData(null, StringUtils.EMPTY, false),
                    new TestData(StringUtils.EMPTY, StringUtils.EMPTY, false)
            };
        }

        /**
         * @return the xRequestedWithHeaderValue
         */
        private String getXRequestedWithHeaderValue() {
            return xRequestedWithHeaderValue;
        }

        /**
         * @return the xrwParamValue
         */
        private String getXrwParamValue() {
            return xrwParamValue;
        }

        /**
         * @return the xmlHttpRequest
         */
        private boolean isXmlHttpRequest() {
            return xmlHttpRequest;
        }


    }
}
