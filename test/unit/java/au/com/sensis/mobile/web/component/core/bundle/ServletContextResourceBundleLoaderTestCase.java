package au.com.sensis.mobile.web.component.core.bundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;

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
    }

    @Test
    public void testLoadFileWhenSuccessful() throws Throwable {

        EasyMock.expect(
                getMockServletContext().getResourceAsStream(
                        "/WEB-INF/resourceThatExists.xml")).andReturn(
                getMockInputStream());

        replay();

        final InputStream actualInputStream =
                getObjectUnderTest()
                        .loadFile("/WEB-INF/resourceThatExists.xml");
        Assert.assertSame("actualInputStream is wrong", getMockInputStream(),
                actualInputStream);
    }

    @Test
    public void testLoadFileWhenUnsuccessful() throws Throwable {

        final String filePath = "/I don't exist";
        EasyMock.expect(getMockServletContext().getResourceAsStream(filePath))
                .andReturn(null);

        replay();

        try {
            getObjectUnderTest().loadFile(filePath);
            Assert.fail("IOException expected.");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    "Failed to load file from servlet context: '" + filePath + "'", e
                            .getMessage());
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

        final String requestedFilePath = "/WEB-INF/resourceThatExists.xml";
        EasyMock.expect(
                getMockServletContext().getResource(
                        "/WEB-INF/resourceThatExists.xml")).andReturn(
                expectedResourceUrl);

        replay();

        final File expectedFile = new File(expectedResourceUrl.getFile());

        Assert.assertEquals("getFileLastModified time is wrong", expectedFile.lastModified(),
                getObjectUnderTest().getFileLastModified(requestedFilePath));
    }

    @Test
    public void testGetFileLastModifiedWhenFileNotFound() throws Throwable {
        final String fileResourcePath = "/I don't exist";

        try {
            getObjectUnderTest().getFileLastModified(fileResourcePath);
            Assert.fail("IOException expected.");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    "Failed to load file from servlet context: '" + fileResourcePath
                            + "'.", e
                            .getMessage());
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
