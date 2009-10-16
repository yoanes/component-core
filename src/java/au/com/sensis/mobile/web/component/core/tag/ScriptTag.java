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
 * Facade to a Volantis mcs:script tag that prevents duplicates from being output.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ScriptTag extends SimpleTagSupport {

    /**
     * Attribute name use to store a map of (id, {@link McsScriptBean}) pairs.
     */
    public static final String MCS_SCRIPT_BEAN_MAP_ATTRIBUTE_NAME
        = ScriptTag.class.getName() + ".mcsScriptBeanMap";

    private String src;
    private String type;
    private String name;

    private McsScriptBeanFactory mcsScriptBeanFactory;

    /**
     * Default constructor.
     */
    public ScriptTag() {
        setMcsScriptBeanFactory(new DefaultMcsScriptBeanFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doTag() throws JspException, IOException {
        initMcsScriptBeanMapInJspContextIfRequired();

        final McsScriptBean mcsScriptBean =
                getMcsScriptBeanFactory().createMcsScriptBean(getSrc(), getName(),
                        getType());

        if (!getMcsScriptBeanMapFromJspContext().containsKey(
                mcsScriptBean.getId())) {
            getMcsScriptBeanMapFromJspContext().put(mcsScriptBean.getId(),
                    mcsScriptBean);
            mcsScriptBean
                    .writeMcsScript(getJspContext().getOut(), getJspBody());

        }
    }

    private void initMcsScriptBeanMapInJspContextIfRequired() {
        if (getMcsScriptBeanMapFromJspContext() == null) {
            getJspContext().setAttribute(MCS_SCRIPT_BEAN_MAP_ATTRIBUTE_NAME,
                    new HashMap<String, McsScriptBean>(),
                    PageContext.REQUEST_SCOPE);
        }
    }

    /**
     * @return Map of ids to {@link McsScriptBean}s obtained from the
     *         JspContext.
     */
    @SuppressWarnings("unchecked")
    private Map<String, McsScriptBean> getMcsScriptBeanMapFromJspContext() {
        return (Map<String, McsScriptBean>) getJspContext().getAttribute(
                MCS_SCRIPT_BEAN_MAP_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE);
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
     * @return the mcsScriptBeanFactory
     */
    private McsScriptBeanFactory getMcsScriptBeanFactory() {
        return mcsScriptBeanFactory;
    }

    /**
     * @param mcsScriptBeanFactory {@link McsScriptBeanFactory} to set.
     */
    protected void setMcsScriptBeanFactory(final McsScriptBeanFactory mcsScriptBeanFactory) {
        this.mcsScriptBeanFactory = mcsScriptBeanFactory;
    }

    /**
     * Factory for {@link McsScriptBean}s to facilitate testing.
     */
    public static interface McsScriptBeanFactory {
        /**
         * Create an {@link McsScriptBean}.
         * @param src See {@link McsScriptBean}.
         * @param name See {@link McsScriptBean}.
         * @param type See {@link McsScriptBean}.
         * @return a new {@link McsScriptBean}.
         */
        McsScriptBean createMcsScriptBean(final String src, final String name, final String type);
    }

    /**
     * Default {@link McsScriptBeanFactory} implementation.
     */
    public static class DefaultMcsScriptBeanFactory implements McsScriptBeanFactory {

        /**
         * {@inheritDoc}
         */
        public McsScriptBean createMcsScriptBean(final String src, final String name,
                final String type) {
            return new McsScriptBean(src, name, type);
        }
    }

    /**
     * {@link TagExtraInfo} implementation for validating the data set into the {@link ScriptTag}.
     */
    public static class ScriptTagExtraInfo extends TagExtraInfo {

        /**
         * {@inheritDoc}
         */
        @Override
        public ValidationMessage[] validate(final TagData data) {
            final Object srcAttr = data.getAttribute("src");
            final Object nameAttr = data.getAttribute("name");
            if ((srcAttr == null) && (nameAttr == null)) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(),
                        "You must set either the src or name attributes but not both. src='"
                                + srcAttr + "'; name='" + nameAttr + "'") };
            } else if ((srcAttr != null) && (nameAttr != null)) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(),
                        "You must set either the src or name attributes but not both. src='"
                                + srcAttr + "'; name='" + nameAttr + "'") };

            } else {
                return null;
            }
        }

    }
}
