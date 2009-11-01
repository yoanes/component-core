package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


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

        if (fileUrl != null) {
            final URLConnection fileURlConnection = fileUrl.openConnection();
            return fileURlConnection.getLastModified();
        } else {
            throw new IOException("Failed to load file from classpath: '"
                    + classpathFilename + "'.");
        }
    }
}
