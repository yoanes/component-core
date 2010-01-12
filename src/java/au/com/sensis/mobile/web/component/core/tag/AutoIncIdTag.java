package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.commons.lang.StringUtils;

/**
 * JSP tag handler for auto incrementing an identifier with a given prefix and
 * setting it into a variable with the given name in
 * {@link PageContext#PAGE_SCOPE}. For example, if prefix="myIdPrefix" and
 * var="myVar", this tag will result in the following sequence of values being
 * set into myVar each time it is called:
 * <ol>
 * <li>Call 0: myVar=="myIdPrefix0"</li>
 * <li>Call 1: myVar=="myIdPrefix1"</li>
 * <li>Call 2: myVar=="myIdPrefix2"</li>
 * </ol>
 *
 * <p>
 * This tag is useful for components to generate predictable ids. As long as
 * {@link #setPrefix(String)} is carefully chosen, ids generated should not
 * conflict with other components or the applications using them. eg. the prefix
 * should include the component name.
 * </p>
 * <p>
 * The approach to id generation also allows a component's JavaScript to
 * sequentially search for all ids with a common prefix (stopping as soon the
 * current id in the sequence is not found) and decorate the found elements with
 * event handlers. <b>This support is crucial with Volantis since event handlers can
 * not be specified in the page markup. All JavaScript must be in the document head.</b>
 * </p>
 *
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class AutoIncIdTag extends SimpleTagSupport {

    private static final int INITIAL_ID_COUNTER = 0;
    private String prefix;
    private String var;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doTag() throws JspException, IOException {
        getJspContext().setAttribute(getVar(), getAutoIncId());
    }

    /**
     * @return
     */
    private String getAutoIncId() {
        return getPrefix() + getAndUpdateAutoIncCounter();
    }

    /**
     * @return
     */
    private int getAndUpdateAutoIncCounter() {
        final Integer idCounter = getCounterFromRequestScopeIfExists();
        if (idCounter == null) {
            setIdCounterIntoRequestScope(INITIAL_ID_COUNTER);
        } else {
            setIdCounterIntoRequestScope(idCounter.intValue() + 1);
        }
        return getCounterFromRequestScopeIfExists().intValue();
    }

    private void setIdCounterIntoRequestScope(final int newIdCounterValue) {
        getJspContext().setAttribute(getIdCounterAttributeName(),
                new Integer(newIdCounterValue), PageContext.REQUEST_SCOPE);
    }

    /**
     * @return
     */
    private Integer getCounterFromRequestScopeIfExists() {
        final Integer idCounter =
                (Integer) getJspContext().getAttribute(
                        getIdCounterAttributeName(), PageContext.REQUEST_SCOPE);
        return idCounter;
    }

    private String getIdCounterAttributeName() {
        return AutoIncIdTag.class.getName() + "." + getPrefix();
    }

    /**
     * Name of the variable to set the result into.
     *
     * @return Name of the variable to set the result into.
     */
    public String getVar() {
        return var;
    }

    /**
     * Name of the variable to set the result into.
     *
     * @param var Name of the variable to set the result into.
     */
    public void setVar(final String var) {
        this.var = var;
    }

    /**
     * Prefix for the generated identifier.
     *
     * @return Prefix for the generated identifier.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Prefix for the generated identifier.
     *
     * @param prefix
     *            Prefix for the generated identifier.
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * {@link TagExtraInfo} implementation for validating the data set into the
     * {@link AutoIncIdTag}.
     */
    public static class AutoIncIdTagExtraInfo extends TagExtraInfo {

        /**
         * {@inheritDoc}
         */
        @Override
        public ValidationMessage[] validate(final TagData data) {
            final String varAttr = data.getAttributeString("var");
            if (StringUtils.isBlank(varAttr)) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(),
                        "You must set the var attribute to a non-blank value: '"
                                + varAttr + "'") };
            }

            final String prefixAttr = data.getAttributeString("prefix");
            if (StringUtils.isBlank(prefixAttr)) {
                return new ValidationMessage[] { new ValidationMessage(data
                        .getId(),
                        "You must set the prefix attribute to a non-blank value: '"
                                + prefixAttr + "'") };
            }

            return null;
        }

    }
}
