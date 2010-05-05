package au.com.sensis.mobile.web.component.core.util;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Simple {@link HttpServletResponseWrapper} that captures the (text) response
 * in a {@link CharArrayWriter}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class CharArrayWriterHttpServletResponseWrapper extends
        HttpServletResponseWrapper {

    private final CharArrayWriter writer;

    /**
     * Default constructor.
     *
     * @param response
     *            {@link HttpServletResponse} to be wrapped.
     * @param initialCharArrayWriterSize
     *            Initial size of the {@link CharArrayWriter} to capture the
     *            response with.
     */
    public CharArrayWriterHttpServletResponseWrapper(
            final HttpServletResponse response,
            final int initialCharArrayWriterSize) {
        super(response);

        writer = new CharArrayWriter(initialCharArrayWriterSize);
    }

    /**
     * @return The content that was written to {@link #getWriter()}.
     */
    public String getWriterAsString() {
        return writer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(writer);
    }

}
