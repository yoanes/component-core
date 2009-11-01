package au.com.sensis.mobile.web.component.core.bundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test {@link ClasspathResourceBundleLoader}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ClasspathResourceBundleLoaderTestCase extends AbstractResourceBundleLoaderTestCase {

    @Test
    public void testLoadFileWhenSuccessful() throws Throwable {

        InputStream actualInputStream = null;

        try {
            actualInputStream = getObjectUnderTest().loadFile(
                    "/au/com/sensis/mobile/web/component/core/bundle/"
                        + "ClasspathResourceBundleLoaderTestCase.class");
        } finally {
            if (actualInputStream != null) {
                actualInputStream.close();
            }
        }
    }

    @Test
    public void testLoadFileWhenUnsuccessful() throws Throwable {

        InputStream actualInputStream = null;

        final String filePath = "/I don't exist";
        try {
            actualInputStream = getObjectUnderTest().loadFile(filePath);
            Assert.fail("IOException expected.");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    "Failed to load file from classpath: '" + filePath + "'", e
                            .getMessage());
        } finally {
            if (actualInputStream != null) {
                actualInputStream.close();
            }
        }

    }

    @Test
    public void testGetFileLastModifiedForFileFoundOnFileSystem() throws Throwable {
        final String fileResourcePath =
                "/au/com/sensis/mobile/web/component/core/bundle/"
                        + "ClasspathResourceBundleLoaderTestCase.class";
        final URL resourceUrl = getClass().getResource(fileResourcePath);
        Assert.assertEquals("This test assumes that '" + fileResourcePath
                + "' can be found on the classpath as a file: URL. "
                + "If not, you may have to modify this test works. Actual URL found: "
                + resourceUrl,
                "file", resourceUrl.getProtocol());

        final File expectedFile = new File(resourceUrl.getFile());

        Assert.assertEquals("getFileLastModified time is wrong", expectedFile.lastModified(),
                getObjectUnderTest().getFileLastModified(fileResourcePath));
    }

    @Test
    public void testGetFileLastModifiedForFileFoundInJar() throws Throwable {
        final String fileResourcePath = "/java/lang/String.class";
        final URL resourceUrl = getClass().getResource(fileResourcePath);
        Assert.assertEquals("This test assumes that '" + fileResourcePath
                + "' can be found on the classpath as a jar: URL. "
                + "If not, you may have to modify this test works. "
                + "Actual URL found: " + resourceUrl, "jar", resourceUrl
                .getProtocol());

        final JarURLConnection jarURLConnection =
                (JarURLConnection) resourceUrl.openConnection();

        Assert.assertTrue("Looks like we can't tell when " + jarURLConnection
                + " was last modified", jarURLConnection.getLastModified() > 0);

        Assert.assertEquals("getFileLastModified time is wrong",
                jarURLConnection.getLastModified(), getObjectUnderTest()
                        .getFileLastModified(fileResourcePath));
    }

    @Test
    public void testGetFileLastModifiedWhenFileNotFound() throws Throwable {
        final String fileResourcePath = "/I don't exist";

        try {
            getObjectUnderTest().getFileLastModified(fileResourcePath);
            Assert.fail("IOException expected.");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    "Failed to load file from classpath: '" + fileResourcePath
                            + "'.", e.getMessage());
        }
    }

    @Override
    protected AbstractResourceBundleLoader createObjectUnderTest() {
        return new ClasspathResourceBundleLoader();
    }
}
