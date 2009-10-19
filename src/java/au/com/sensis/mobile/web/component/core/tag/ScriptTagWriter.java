package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * Interface that knows how to write out a script tag instance (ie. with
 * specific attribute values) to a JSP. The target markup dialect is
 * implementation dependent. eg. XDIME2, XHTML etc.
 */
public interface ScriptTagWriter {

    /**
     * @return The unique identifier to associate with this writer. This may be
     *         used to detect whether a writer has already been invoked. ie.
     *         clients can prevent duplicate tags from being output.
     */
    String getId();

    /**
     * @return src attribute of the script tag that this writer is to output.
     */
    String getSrc();

    /**
     * @return name attribute of the script tag that this writer is to output.
     */
    String getName();

    /**
     * @return type attribute of the script tag that this writer is to output.
     */
    String getType();

    /**
     * Writes out an mcs:script element.
     *
     * @param jspWriter
     *            {@link JspWriter} to write to.
     * @param jspBody
     *            {@link JspFragment} fragment representing the body of the tag.
     * @throws IOException
     *             Thrown if an IO error occurs.
     * @throws JspException
     *             Thrown if any other error occurs.
     */
    void writeScript(final JspWriter jspWriter, final JspFragment jspBody)
            throws IOException, JspException;

}
