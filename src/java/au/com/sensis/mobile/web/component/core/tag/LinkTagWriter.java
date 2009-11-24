package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * Interface that knows how to write out a link tag instance (ie. with
 * specific attribute values) to a JSP.
 */
public interface LinkTagWriter {

    /**
     * @return The unique identifier to associate with this writer. This may be
     *         used to detect whether a writer has already been invoked. ie.
     *         clients can prevent duplicate tags from being output.
     */
    String getId();

    /**
     * @return href attribute of the link tag that this writer is to output.
     */
    String getHref();

    /**
     * @return List of the {@link DynamicTagAttribute}s for the
     *         link tag to be written.
     */
    List<DynamicTagAttribute> getDynamicAttributes();

    /**
     * Writes out a link tag.
     *
     * @param jspWriter
     *            {@link JspWriter} to write to.
     * @throws IOException
     *             Thrown if an IO error occurs.
     * @throws JspException
     *             Thrown if any other error occurs.
     */
    void writeTag(final JspWriter jspWriter)
            throws IOException, JspException;

}
