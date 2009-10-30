package au.com.sensis.mobile.web.component.core.bundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletContext;


/**
 * {@link AbstractResourceBundleLoader} that loads resource bundles from from
 * the {@link ServletContext}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ServletContextResourceBundleLoader extends AbstractResourceBundleLoader
        implements ResourceBundleLoader {

    private ServletContext servletContext;

    /**
     * {@inheritDoc}
     */
    @Override
    protected InputStream loadFile(final String servletContextFileName)
        throws IOException {
        final InputStream loadedFileInputStream =
                getServletContext().getResourceAsStream(servletContextFileName);

        if (loadedFileInputStream != null) {
            return loadedFileInputStream;
        } else {
            throw new IOException(
                    "Failed to load file from servlet context: '"
                            + servletContextFileName + "'");
        }
    }

    /**
     * @return the servletContext
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * @param servletContext
     *            the servletContext to set
     */
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getFileLastModified(final String servletContextFileName)
            throws IOException {
        final URL fileUrl = getServletContext().getResource(servletContextFileName);

        if ((fileUrl != null) && "file".equals(fileUrl.getProtocol())) {
            final File file = new File(fileUrl.getFile());
            return file.lastModified();
        } else {
            throw new IOException(
                    "Failed to load file from servlet context: '" + servletContextFileName
                            + "'. Only found URL: '" + fileUrl
                            + "' but expected it to be a 'file:' URL.");
        }

    }
}
