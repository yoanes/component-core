package au.com.sensis.mobile.web.component.core.bundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;


/**
 * {@link AbstractResourceBundleLoader} that loads resource bundles from the classpath.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ClasspathResourceBundleLoader
    extends AbstractResourceBundleLoader implements ResourceBundleLoader {

    /**
     * {@inheritDoc}
     */
    @Override
    protected InputStream loadFile(final String classpathFilename)
            throws IOException {
        final InputStream loadedFileInputStream =
                getClass().getResourceAsStream(classpathFilename);

        if (loadedFileInputStream != null) {
            return loadedFileInputStream;
        } else {
            throw new IOException(
                    "Failed to load file from classpath: '" + classpathFilename
                            + "'");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getFileLastModified(final String classpathFilename)
            throws IOException {
        final URL fileUrl = getClass().getResource(classpathFilename);

        if ((fileUrl != null) && "file".equals(fileUrl.getProtocol())) {
            final File file = new File(fileUrl.getFile());
            return file.lastModified();
        } else if ((fileUrl != null) && "jar".equals(fileUrl.getProtocol())) {
            final JarURLConnection jarURLConnection =
                    (JarURLConnection) fileUrl.openConnection();
            return jarURLConnection.getLastModified();
        } else {
            throw new IOException(
                    "Failed to load file from classpath: '" + classpathFilename
                            + "'. Only found URL: '" + fileUrl
                            + "' but expected it to be a 'file:' or 'jar:' URL.");
        }
    }
}
