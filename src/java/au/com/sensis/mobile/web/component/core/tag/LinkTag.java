package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Facade to a link tag. This facade prevents duplicate tags from being written in the current
 * HTTP request. The unique id of each tag is governed by the value of the href attribute.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class LinkTag extends SimpleTagSupport implements DynamicAttributes {

    /**
     * Attribute name used to store a map of ({@link LinkTagWriter#getId()},
     * {@link LinkTagWriter}) pairs.
     */
    public static final String LINK_WRITER_MAP_ATTRIBUTE_NAME =
            ScriptTag.class.getName() + ".linkTagWriterMap";

    private String href;

    private final List<DynamicTagAttribute> dynamicAttributes
        = new ArrayList<DynamicTagAttribute>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void doTag() throws JspException, IOException {
        initScriptWriterMapInJspContextIfRequired();

        final LinkTagWriter linkTagWriter =
                new SimpleLinkTagWriter(getDynamicAttributes(), getHref());
        if (!getLinkTagWriterMapFromJspContext().containsKey(
                linkTagWriter.getId())) {
            linkTagWriter.writeTag(getJspContext().getOut());
            getLinkTagWriterMapFromJspContext().put(linkTagWriter.getId(),
                    linkTagWriter);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setDynamicAttribute(final String uri, final String localName,
            final Object value) throws JspException {
        getDynamicAttributes().add(
                new DynamicTagAttribute(localName, value));
    }

    private void initScriptWriterMapInJspContextIfRequired() {
        if (getLinkTagWriterMapFromJspContext() == null) {
            getJspContext().setAttribute(LINK_WRITER_MAP_ATTRIBUTE_NAME,
                    new HashMap<String, LinkTagWriter>(),
                    PageContext.REQUEST_SCOPE);
        }
    }

    /**
     * @return Map of ids to {@link McsScriptTagWriter}s obtained from the
     *         JspContext.
     */
    @SuppressWarnings("unchecked")
    private Map<String, LinkTagWriter> getLinkTagWriterMapFromJspContext() {
        return (Map<String, LinkTagWriter>) getJspContext().getAttribute(
                LINK_WRITER_MAP_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE);
    }

    /**
     * @return the href
     */
    public String getHref() {
        return href;
    }

    /**
     * @param href the href to set
     */
    public void setHref(final String href) {
        this.href = href;
    }

    /**
     * @return the dynamicAttributes
     */
    private List<DynamicTagAttribute> getDynamicAttributes() {
        return dynamicAttributes;
    }
}
