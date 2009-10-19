package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * {@link ScriptTagWriter} implementation that knows how to output Volantis mcs:script tags.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public final class McsScriptTagWriter implements ScriptTagWriter {

    private final String src;
    private final String name;
    private final String type;

    /**
     * Default constructor.
     * @param src Value of the src attribute of the mcs:script.
     * @param name Name to associate with the element, in the case that the src attribute is blank.
     * @param type Value of the type attribute of the mcs:script.
     */
    public McsScriptTagWriter(final String src, final String name, final String type) {
        this.src = src;
        this.name = name;
        this.type = type;

    }

    /**
     * {@inheritDoc}
     *
     * This implementation uses the algorithm: If {@link #getSrc()} is not
     * blank, it is used. Otherwise, {@link #getName()} is used.
     */
    public String getId() {
        if (StringUtils.isNotBlank(getSrc())) {
            return getSrc();
        } else {
            return getName();
        }
    }

    /**
     * @return the src
     */
    public String getSrc() {
        return src;
    }

    /**
     * {@inheritDoc}
     */
    public void writeScript(final JspWriter jspWriter, final JspFragment jspBody)
            throws IOException, JspException {
        jspWriter.println("<mcs:script ");

        if (StringUtils.isNotBlank(getType())) {
            jspWriter.println("type=\"" + getType() + "\" ");
        }

        if (StringUtils.isNotBlank(getSrc())) {
            jspWriter.println("src=\"" + getSrc() + "\"/>");
        } else {
            jspWriter.println(">");
            jspBody.invoke(jspWriter);
            jspWriter.println("</mcs:script>");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || !this.getClass().equals(obj.getClass())) {
            return false;
        }

        final McsScriptTagWriter rhs = (McsScriptTagWriter) obj;
        final EqualsBuilder equalsBuilder = new EqualsBuilder();

        equalsBuilder.append(src, rhs.src);
        equalsBuilder.append(name, rhs.name);
        equalsBuilder.append(type, rhs.type);
        return equalsBuilder.isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(src);
        hashCodeBuilder.append(name);
        hashCodeBuilder.append(type);
        return hashCodeBuilder.toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("src", getSrc())
            .append("name", getName())
            .append("type", getType())
            .toString();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

}
