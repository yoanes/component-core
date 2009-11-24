package au.com.sensis.mobile.web.component.core.tag;


/**
 * Simple bean representing a dynamic tag attribute set into a
 * {@link javax.servlet.jsp.tagext.DynamicAttributes} implementation.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class DynamicTagAttribute {

    private final String namespaceUri;
    private final String localName;
    private final Object value;

    /**
     * Constructor which assumes that {@link #getNamespaceUri()} is null (ie. default
     * namespace).
     *
     * @param localName name of the attribute being set.
     * @param value value of the attribute
     */
    public DynamicTagAttribute(final String localName, final Object value) {
        this(null, localName, value);
    }

    /**
     * Default constructor.
     *
     * @param namespaceUri namespace of the attribute, or null if in the default namespace.
     * @param localName name of the attribute being set.
     * @param value value of the attribute
     */
    public DynamicTagAttribute(final String namespaceUri,
            final String localName, final Object value) {
        this.namespaceUri = namespaceUri;
        this.localName = localName;
        this.value = value;
    }

    /**
     * @return the namespaceUri
     */
    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
     * @return the localName
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

}
