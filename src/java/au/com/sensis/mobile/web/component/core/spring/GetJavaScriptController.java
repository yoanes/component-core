package au.com.sensis.mobile.web.component.core.spring;

import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

public class GetJavaScriptController extends AbstractResourceLoaderController {
	
	private static final Logger logger = Logger.getLogger(GetJavaScriptController.class);
	
	private static final long serialVersionUID = 1L;

	private static final String REQUEST_URI_PREFIX = "/js/";
	
	private String contextRootComponentPrefix;

	@Override
	protected ModelAndView doHandleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("request.getRequestURI(): '" + request.getRequestURI() + "'");
		}

		String javascriptFilenameRequested =
			extractFilenameRequested(request, REQUEST_URI_PREFIX);
		
		InputStream javascriptBundleInputStream; 
		if (isExplodedJsRequested(request)){
			javascriptBundleInputStream = getResourceLoader().loadJavaScriptBundleExplodedFilesLoader(javascriptFilenameRequested);
			
			PrintWriter printWriter = response.getWriter();
			printWriter.println("var jsPathPrefix='" + getContextRootComponentPrefix() + "';");
		} else {
			javascriptBundleInputStream = getResourceLoader().loadJavaScriptBundle(javascriptFilenameRequested);
		}
		
		writeInputStreamToResponse(response, javascriptBundleInputStream);
		
		return null;
	}


	/**
	 * @param request
	 * @return
	 */
	private boolean isExplodedJsRequested(HttpServletRequest request) {
		return "true".equals(request.getHeader("x-sensis-js-exploded"));
	}
	
	/**
	 * @return the logger
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}

	/**
	 * @return the contextRootComponentPrefix
	 */
	public String getContextRootComponentPrefix() {
		return contextRootComponentPrefix;
	}


	/**
	 * @param contextRootComponentPrefix the contextRootComponentPrefix to set
	 */
	public void setContextRootComponentPrefix(String contextRootComponentPrefix) {
		this.contextRootComponentPrefix = contextRootComponentPrefix;
	}
}
