package au.com.sensis.mobile.web.component.core.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

public class GetPrintWriterMapAsync extends AbstractCommandController {
	
	private static final Logger logger = Logger.getLogger(GetPrintWriterMapAsync.class);
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object commandObject, BindException bindException)
			throws Exception {
//		if (logger.isDebugEnabled()) {
//			logger.debug("MapCriteria: " + commandObject);
//		}
//		
//		if (logger.isDebugEnabled()) {
//			logger.debug("About to execute dummyMapService");
//		}
//
//		String dummyMapServiceResponse = getDummyMapService().execute();
//		
//		PrintWriter printWriter = response.getWriter();
//		printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
//		printWriter.println("<map>");
//		printWriter.println("<url>Spring Controller - PrintWriter written url from dummyMapService:");
//		printWriter.println(dummyMapServiceResponse);
//		printWriter.println("</url>");
//		printWriter.println("</map>");
//		response.setContentType("text/xml");
		return null;
	}

//	/**
//	 * @return the dummyMapService
//	 */
//	public DummyMapService getDummyMapService() {
//		return dummyMapService;
//	}
//
//	/**
//	 * @param dummyMapService the dummyMapService to set
//	 */
//	public void setDummyMapService(DummyMapService dummyMapService) {
//		this.dummyMapService = dummyMapService;
//	}
//
	
}
