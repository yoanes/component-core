package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import au.com.sensis.mobile.crf.presentation.tag.DynamicTagAttribute;

/**
 * Simple implementation of {@link LinkTagWriter} that outputs a link tag in the default namespace.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class SimpleLinkTagWriter implements LinkTagWriter {

    private final List<DynamicTagAttribute> dynamicAttributes;
    private final String href;

    /**
     * Default constructor.
     *
     * @param dynamicAttributes
     *            List of {@link DynamicTagAttribute}s containing dynamic JSP
     *            tag attributes to be written out.
     * @param href
     *            Href attribute of the tag to be written.
     */
    public SimpleLinkTagWriter(
            final List<DynamicTagAttribute> dynamicAttributes, final String href) {
        this.dynamicAttributes = dynamicAttributes;
        this.href = href;
    }

    /**
     * {@inheritDoc}
     */
    public List<DynamicTagAttribute> getDynamicAttributes() {
        return dynamicAttributes;
    }

    /**
     * {@inheritDoc}
     */
    public String getHref() {
        return href;
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return getHref();
    }

    /**
     * {@inheritDoc}
     */
    public void writeTag(final JspWriter jspWriter)
            throws IOException, JspException {
        jspWriter.print("<link ");

        jspWriter.print("href=\"" + getHref() + "\" ");

        for (final DynamicTagAttribute attribute : getDynamicAttributes()) {
            jspWriter.print(attribute.getLocalName() + "=\"" + attribute.getValue() + "\" ");
        }

        jspWriter.print("/>");

    }

}
