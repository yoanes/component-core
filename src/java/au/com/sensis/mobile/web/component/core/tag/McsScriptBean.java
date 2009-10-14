package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/* package */ final class McsScriptBean {

	private final String src;
	private final String name;
	private final String type;

	/* package */ McsScriptBean(String src, String name, String type) {
		this.src = src;
		this.name = name;
		this.type = type;
		
	}
	
	/* package */ String getId() {
		if (StringUtils.isNotBlank(getSrc())) {
			return getSrc();
		} else {
			return getName();
		}
	}

	/**
	 * @return the src
	 */
	/* package */ String getSrc() {
		return src;
	}
	
	/* package */ void writeMcsScript(JspWriter jspWriter, JspFragment jspBody) throws IOException, JspException {
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
     * Implementation of the equals method that compares every field.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || !this.getClass().equals(obj.getClass())) {
            return false;
        }

        McsScriptBean rhs = (McsScriptBean) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();

        equalsBuilder.append(this.src, rhs.src);
        return equalsBuilder.isEquals();
    }

    /**
     * Implementation of hashCode that uses every field. (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.src);
        return hashCodeBuilder.toHashCode();
    }
    
    /**
     * Produces a String using {@link ReflectionToStringBuilder}.
     */
    @Override
    public String toString() {
    	return new ToStringBuilder(this).append("src", getSrc()).toString();
    }

	/**
	 * @return the name
	 */
	/* package */ String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	/* package */ String getType() {
		return type;
	}


	
}
