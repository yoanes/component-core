package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

/**
 * Facade to a markup specific script tag. This facade uses a
 * {@link ScriptTagWriterFactory} to determine which target markup dialect to
 * output. The {@link ScriptTagWriterFactory} implementation used is the first
 * of the following that is found to be non-null:
 * <ol>
 * <li>{@link #getScriptTagWriterFactory()}</li>
 * <li>Value of the {@link #SCRIPT_WRITER_FACTORY_ATTRIBUTE_NAME} attribute
 * found by calling {@link javax.servlet.jsp.JspContext#findAttribute(String)}.</li>
 * <li>A built-in default, currently
 * {@link VolantisDialect#getScriptTagWriterFactory()}.</li>
 * </ol>
 *
 * This facade also prevents duplicate tags from being written in the current
 * HTTP request. The unique id of each tag is governed by
 * {@link ScriptTagWriter#getId()}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ScriptTag extends SimpleTagSupport {

    /**
     * Attribute name used to store a map of ({@link ScriptTagWriter#getId()},
     * {@link ScriptTagWriter}) pairs.
     */
    public static final String SCRIPT_WRITER_MAP_ATTRIBUTE_NAME =
            ScriptTag.class.getName() + ".scriptWriterMap";

    /**
     * Attribute name used to lookup the application controlled default
     * {@link ScriptTagWriterFactory} implementation.
     */
    public static final String SCRIPT_WRITER_FACTORY_ATTRIBUTE_NAME =
            ScriptTag.class.getName() + ".scriptWriterFactory";

    private String src;
    private String type;
    private String name;

    private ScriptTagWriterFactory scriptTagWriterFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doTag() throws JspException, IOException {
        initScriptWriterMapInJspContextIfRequired();

        final ScriptTagWriter scriptTagWriter =
                findScriptTagWriterFactory().createScriptTagWriter(getSrc(),
                        getName(), getType());

        if (!getScriptTagWriterMapFromJspContext().containsKey(
                scriptTagWriter.getId())) {
            scriptTagWriter.writeScript(getJspContext().getOut(), getJspBody());
            getScriptTagWriterMapFromJspContext().put(scriptTagWriter.getId(),
                    scriptTagWriter);

        }
    }

    private void initScriptWriterMapInJspContextIfRequired() {
        if (getScriptTagWriterMapFromJspContext() == null) {
            getJspContext().setAttribute(SCRIPT_WRITER_MAP_ATTRIBUTE_NAME,
                    new HashMap<String, McsScriptTagWriter>(),
                    PageContext.REQUEST_SCOPE);
        }
    }

    /**
     * @return Map of ids to {@link McsScriptTagWriter}s obtained from the
     *         JspContext.
     */
    @SuppressWarnings("unchecked")
    private Map<String, ScriptTagWriter> getScriptTagWriterMapFromJspContext() {
        return (Map<String, ScriptTagWriter>) getJspContext().getAttribute(
                SCRIPT_WRITER_MAP_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE);
    }

    /**
     * @return the src
     */
    public String getSrc() {
        return src;
    }

    /**
     * @param src
     *            the src to set
     */
    public void setSrc(final String src) {
        this.src = src;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the scriptTagWriterFactory
     */
    public ScriptTagWriterFactory getScriptTagWriterFactory() {
        return scriptTagWriterFactory;
    }

    /**
     * NOTE: this method is private since the shorter
     * {@link #setTagFactory(ScriptTagWriterFactory)} is exposed as a JSP tag
     * attribute.
     *
     * @param scriptTagWriterFactory
     *            {@link ScriptTagWriterFactory} to set.
     */
    public void setScriptTagWriterFactory(
            final ScriptTagWriterFactory scriptTagWriterFactory) {
        this.scriptTagWriterFactory = scriptTagWriterFactory;
    }

    /**
     * @return ScriptTagWriterFactory to use to create a {@link ScriptTagWriter}
     *         .
     */
    private ScriptTagWriterFactory findScriptTagWriterFactory() {
        if (getScriptTagWriterFactory() != null) {
            return getScriptTagWriterFactory();
        } else if (findScriptTagWriterFactoryJspAttribute() != null) {
            return findScriptTagWriterFactoryJspAttribute();
        } else {
            return VolantisDialect.getInstance().getScriptTagWriterFactory();
        }
    }

    private ScriptTagWriterFactory findScriptTagWriterFactoryJspAttribute() {
        return (ScriptTagWriterFactory) getJspContext().findAttribute(
                SCRIPT_WRITER_FACTORY_ATTRIBUTE_NAME);
    }

    /**
     * {@link TagExtraInfo} implementation for validating the data set into the
     * {@link ScriptTag}.
     */
    public static class ScriptTagExtraInfo extends TagExtraInfo {

        /**
         * {@inheritDoc}
         */
        @Override
        public ValidationMessage[] validate(final TagData data) {
            final Object srcAttr = data.getAttribute("src");
            final Object nameAttr = data.getAttribute("name");
            if (bothNullOrBothNotNull(srcAttr, nameAttr)) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(),
                        "You must set either the src or name attributes but not both. src='"
                                + srcAttr + "'; name='" + nameAttr + "'") };
            } else {
                return null;
            }
        }

        /**
         * @param srcAttr
         * @param nameAttr
         * @return
         */
        private boolean bothNullOrBothNotNull(final Object srcAttr,
                final Object nameAttr) {
            return ((srcAttr == null) && (nameAttr == null))
                    || ((srcAttr != null) && (nameAttr != null));
        }

    }
}
