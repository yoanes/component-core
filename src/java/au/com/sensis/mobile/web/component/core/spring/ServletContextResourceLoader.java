package au.com.sensis.mobile.web.component.core.spring;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

public class ServletContextResourceLoader extends AbstractResourceLoader implements ResourceLoader {
	
	private Logger logger = Logger.getLogger(ServletContextResourceLoader.class);
	
	private ServletContext servletContext;
	
	@Override
	protected InputStream loadFile(String servletContextFileName) {
		InputStream loadedFileInputStream =
				getServletContext().getResourceAsStream(servletContextFileName);
		if (logger.isDebugEnabled()) {
			logger.debug("loadedFileInputStream: '"
					+ loadedFileInputStream + "'");
		}
		
		if (loadedFileInputStream != null) {
			return loadedFileInputStream;
		} else {
			throw new RuntimeException("Failed to load file from servlet context: " + servletContextFileName);
		}
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}
