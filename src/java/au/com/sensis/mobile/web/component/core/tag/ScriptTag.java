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

import org.apache.commons.lang.StringUtils;

public class ScriptTag extends SimpleTagSupport {

	private static final String PAGE_CONTEXT_ATTRIBUTE_NAME =
			SimpleTagSupport.class.getPackage() + "."
					+ SimpleTagSupport.class.getName() + ".mcsScriptBeanMap";

	private String src;
	private String type;
	private String name;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {
		if (getMcsScriptBeanMapFromPageContext() == null) {
			initMcsScriptBeanMapInPageContext();
		}

		final McsScriptBean mcsScriptBean =
				new McsScriptBean(getSrc(), getName(), getType());

		if (!getMcsScriptBeanMapFromPageContext().containsKey(
				mcsScriptBean.getId())) {
			getMcsScriptBeanMapFromPageContext().put(mcsScriptBean.getId(),
					mcsScriptBean);
			mcsScriptBean
					.writeMcsScript(getJspContext().getOut(), getJspBody());

		}
	}

	private void initMcsScriptBeanMapInPageContext() {
		getJspContext()
				.setAttribute(PAGE_CONTEXT_ATTRIBUTE_NAME,
						new HashMap<String, McsScriptBean>(),
						PageContext.REQUEST_SCOPE);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, McsScriptBean> getMcsScriptBeanMapFromPageContext() {
		return (Map<String, McsScriptBean>) getJspContext().getAttribute(PAGE_CONTEXT_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE);
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
	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public static class ScriptTagExtraInfo extends TagExtraInfo {

		/**
		 * @see javax.servlet.jsp.tagext.TagExtraInfo#validate(javax.servlet.jsp.tagext.TagData)
		 */
		@Override
		public ValidationMessage[] validate(TagData data) {
			Object srcAttr = data.getAttribute("src");
			Object nameAttr = data.getAttribute("name");
			if (srcAttr == null && nameAttr == null) {
				return new ValidationMessage[] { new ValidationMessage(data
						.getId(),
						"You must set either the src or name attributes but not both. src='"
								+ srcAttr + "'; name='" + nameAttr + "'") };
			} else if (srcAttr != null && nameAttr != null) {
				return new ValidationMessage[] { new ValidationMessage(data
						.getId(),
						"You must set either the src or name attributes but not both. src='"
								+ srcAttr + "'; name='" + nameAttr + "'") };

			} else {
				return null;
			}
//			String srcAttr = data.getAttributeString("src");
//			String nameAttr = data.getAttributeString("name");
//			if (StringUtils.isBlank(srcAttr) && StringUtils.isBlank(nameAttr)) {
//				return new ValidationMessage[] { new ValidationMessage(data
//						.getId(),
//						"You must set either the src or name attributes but not both. src='"
//						+ srcAttr + "'; name='" + nameAttr + "'") };
//			} else if (StringUtils.isNotBlank(srcAttr)
//					&& StringUtils.isNotBlank(nameAttr)) {
//				return new ValidationMessage[] { new ValidationMessage(data
//						.getId(),
//						"You must set either the src or name attributes but not both. src='"
//						+ srcAttr + "'; name='" + nameAttr + "'") };
//				
//			} else {
//				return null;
//			}
		}
		
		
		
	}
}
