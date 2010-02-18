package au.com.sensis.mobile.web.component.core.bundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test {@link ClasspathResourceBundleLoader}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ServletContextResourceBundleLoaderTestCase
    extends AbstractResourceBundleLoaderTestCase {

    private static final String DEFAULT_PREFIX = "/WEB-INF/classes/";

    private ServletContext mockServletContext;

    /**
     * Setup test data.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        ((ServletContextResourceBundleLoader) getObjectUnderTest())
                .setServletContext(getMockServletContext());
        ((ServletContextResourceBundleLoader) getObjectUnderTest())
                .setServletContextPathPrefix(DEFAULT_PREFIX);
    }

    @Test
    public void testLoadFileWhenSuccessful() throws Throwable {
        final String requestedFilePath = "au/com/sensis/resourceThatExists.xml";
        final String[] testPathPrefixes =
                { null, StringUtils.EMPTY, "/WEB-INF/classes/" };
        final String[] expectedResourcePath =
            { requestedFilePath, requestedFilePath,
                "/WEB-INF/classes/" + requestedFilePath};

        for (int i = 0; i < testPathPrefixes.length; i++) {

            ((ServletContextResourceBundleLoader) getObjectUnderTest())
                    .setServletContextPathPrefix(testPathPrefixes[i]);

            EasyMock.expect(
                    getMockServletContext().getResourceAsStream(
                            expectedResourcePath[i])).andReturn(
                    getMockInputStream());

            replay();

            final InputStream actualInputStream =
                    getObjectUnderTest().loadFile(
                            requestedFilePath);
            Assert.assertSame("actualInputStream is wrong for test " + i,
                    getMockInputStream(), actualInputStream);

            verify();

            // Reset mocks prior to next iteration.
            setReplayed(false);
            reset();
        }
    }

    @Test
    public void testLoadFileWhenUnsuccessful() throws Throwable {

        final String fileResourcePath = "/I don't exist";
        EasyMock.expect(
                getMockServletContext().getResourceAsStream(
                        DEFAULT_PREFIX + fileResourcePath)).andReturn(null);

        replay();

        try {
            getObjectUnderTest().loadFile(fileResourcePath);
            Assert.fail("IOException expected.");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    "Failed to load file from servlet context: '"
                            + DEFAULT_PREFIX + fileResourcePath
                            + "'. Prefix is '" + DEFAULT_PREFIX
                            + "'. Requested file is '" + fileResourcePath
                            + "'.", e.getMessage());
        }
    }

    @Test
    public void testGetFileLastModifiedForFileFoundOnFileSystem() throws Throwable {
        final String expectedResourceUrlPath =
            "/au/com/sensis/mobile/web/component/core/bundle/"
            + "ServletContextResourceBundleLoaderTestCase.class";
        final URL expectedResourceUrl = getClass().getResource(expectedResourceUrlPath);
        Assert.assertEquals("This test assumes that '" + expectedResourceUrlPath
                + "' can be found on the classpath as a file: URL. "
                + "If not, you may have to modify this test works. Actual URL found: "
                + expectedResourceUrl,
                "file", expectedResourceUrl.getProtocol());


        final String requestedFilePath = "au/com/sensis/resourceThatExists.xml";
        final String[] testPathPrefixes =
                { null, StringUtils.EMPTY, "/WEB-INF/classes/" };
        final String[] expectedResourcePath =
            { requestedFilePath, requestedFilePath,
                "/WEB-INF/classes/" + requestedFilePath};

        for (int i = 0; i < testPathPrefixes.length; i++) {

            ((ServletContextResourceBundleLoader) getObjectUnderTest())
                .setServletContextPathPrefix(testPathPrefixes[i]);

            EasyMock.expect(
                    getMockServletContext().getResource(
                            expectedResourcePath[i])).andReturn(
                    expectedResourceUrl);

            replay();

            final File expectedFile = new File(expectedResourceUrl.getFile());

            Assert.assertEquals("getFileLastModified time is wrong for test " + i,
                    expectedFile.lastModified(),
                    getObjectUnderTest().getFileLastModified(requestedFilePath));

            // Reset mocks prior to next iteration.
            setReplayed(false);
            reset();
        }
    }

    @Test
    public void testGetFileLastModifiedWhenFileNotFound() throws Throwable {
        final String fileResourcePath = "/I don't exist";

        try {
            getObjectUnderTest().getFileLastModified(fileResourcePath);
            Assert.fail("IOException expected.");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    "Failed to load file from servlet context: '"
                            + DEFAULT_PREFIX + fileResourcePath
                            + "'. Prefix is '" + DEFAULT_PREFIX
                            + "'. Requested file is '" + fileResourcePath
                            + "'.", e.getMessage());
        }
    }

    @Override
    protected AbstractResourceBundleLoader createObjectUnderTest() {
        return new ServletContextResourceBundleLoader();
    }

    /**
     * @return the mockServletContext
     */
    public ServletContext getMockServletContext() {
        return mockServletContext;
    }

    /**
     * @param mockServletContext the mockServletContext to set
     */
    public void setMockServletContext(final ServletContext mockServletContext) {
        this.mockServletContext = mockServletContext;
    }
}
