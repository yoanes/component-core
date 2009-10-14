package au.com.sensis.mobile.web.component.core.spring;


import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

public class GetExplodedJavaScriptController extends AbstractResourceLoaderController {
	
	private static final Logger logger = Logger.getLogger(GetExplodedJavaScriptController.class);
	
	private static final long serialVersionUID = 1L;

	private static final String REQUEST_URI_PREFIX = "/jsexploded/"; 
	
	
	
	@Override
	protected ModelAndView doHandleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("request.getRequestURI(): '" + request.getRequestURI() + "'");
		}

		String javascriptFilenameRequested =
				extractFilenameRequested(request, REQUEST_URI_PREFIX);
		
		InputStream javascriptBundleInputStream = getResourceLoader().loadSingleExlpodedJavaScriptFile(javascriptFilenameRequested);
		
		writeInputStreamToResponse(response, javascriptBundleInputStream);
		
		return null;
	}


	/**
	 * @return the logger
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}
}
