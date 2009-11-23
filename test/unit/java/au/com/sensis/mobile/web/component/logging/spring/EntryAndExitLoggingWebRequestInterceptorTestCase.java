package au.com.sensis.mobile.web.component.logging.spring;

import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;
import au.com.sensis.wireless.web.common.validation.ValidatableTestUtils;

/**
 * Unit test {@link EntryAndExitLoggingWebRequestInterceptor}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class EntryAndExitLoggingWebRequestInterceptorTestCase extends AbstractJUnit4TestCase {

    private static final String EXPECTED_NDC_MESSAGE = "my ndc message";

    private static final String EXPECTED_REQUEST_URI = "http://some.url.com";

    private EntryAndExitLoggingWebRequestInterceptor objectUnderTest;

    private WebRequest webRequest;

    private PatternLayout patternLayout;
    private WriterAppender writerAppender;
    private StringWriter stringWriter;

    private Logger expectedLogger;


    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        patternLayout = new PatternLayout("%p {%x} : %m");
        stringWriter = new StringWriter();
        writerAppender = new WriterAppender(patternLayout, stringWriter);

        expectedLogger = Logger.getLogger(EntryAndExitLoggingWebRequestInterceptor.class);
        expectedLogger.addAppender(writerAppender);
        expectedLogger.setLevel(Level.INFO);

        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI(EXPECTED_REQUEST_URI);
        webRequest = new ServletWebRequest(mockHttpServletRequest);

        objectUnderTest = new EntryAndExitLoggingWebRequestInterceptor();
        objectUnderTest.setNdcMessage(EXPECTED_NDC_MESSAGE);
    }

    /**
     * Tear down test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @After
    public void tearDown() throws Exception {
        NDC.remove();
    }

    @Test
    public void testValidateStateWhenNdcMessageBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };

        for (final String testValue : testValues) {
            objectUnderTest.setNdcMessage(testValue);

            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Failure for testValue: '" + testValue + "'",
                    objectUnderTest, "ndcMessage");
        }

    }

    @Test
    public void testValidateStateWhenSuccessful() throws Throwable {
        objectUnderTest.validateState();
    }

    @Test
    public void testGetNestedDiagnosticContextMessage() throws Throwable {
        Assert.assertEquals("NDC message is wrong", EXPECTED_NDC_MESSAGE,
                objectUnderTest.getNestedDiagnosticContextMessage(getWebRequest()));
    }

    @Test
    public void testPreHandleWhenInfoLoggingEnabled() throws Throwable {
        objectUnderTest.preHandle(getWebRequest());

        Assert.assertEquals("Log message is wrong",
                "INFO {" + EXPECTED_NDC_MESSAGE + "} : Entering uri="
                        + EXPECTED_REQUEST_URI, stringWriter.toString());
    }

    @Test
    public void testPreHandleWhenInfoLoggingDisabled() throws Throwable {
        expectedLogger.setLevel(Level.ERROR);
        objectUnderTest.preHandle(getWebRequest());

        Assert.assertEquals("Log message is wrong",
                StringUtils.EMPTY, stringWriter.toString());
    }

    @Test
    public void testPostHandleWhenInfoLoggingEnabled() throws Throwable {
        // Ensure the NDC is set up as per the real workflow of the interceptor.
        NDC.push(EXPECTED_NDC_MESSAGE);

        objectUnderTest.postHandle(webRequest, new ModelMap());

        Assert.assertEquals("Log message is wrong", "INFO {"
                + EXPECTED_NDC_MESSAGE + "} : Exiting uri="
                + EXPECTED_REQUEST_URI, stringWriter.toString());
    }

    @Test
    public void testPostHandleWhenInfoLoggingDisabled() throws Throwable {
        expectedLogger.setLevel(Level.ERROR);
        objectUnderTest.postHandle(getWebRequest(), new ModelMap());

        Assert.assertEquals("Log message is wrong",
                StringUtils.EMPTY, stringWriter.toString());
    }

    /**
     * @return the webRequest
     */
    public WebRequest getWebRequest() {
        return webRequest;
    }
}
