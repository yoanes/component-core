package au.com.sensis.mobile.web.component.core.sdpcommon.web.filter;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Support capturing the response in addition to the normal behaviour.
 *
 * @author Boyd Sharrock (cloned from TPM)
 */
public class ServletResponseCaptureWrapper
        extends HttpServletResponseWrapper {

    private final CharArrayWriter output = new CharArrayWriter();

    // Creation //

    /**
     * Creation...
     *
     * @param response  the {@link HttpServletResponse}.
     */
    public ServletResponseCaptureWrapper(final HttpServletResponse response) {

        super(response);
    }

    // Operations //

    /**
     * @return  a {@link PrintWriter} which uses a {@link CharArrayWriter} to capture all output.
     */
    public PrintWriter getWriter() {

        return new PrintWriter(output);
    }

    /**
     * @return  the output that was captured by the {@link CharArrayWriter}.
     */
    public String getCapturedOutput() {

        return output.toString();
    }
}
