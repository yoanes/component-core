package au.com.sensis.mobile.web.component.core.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public abstract class AbstractResourceLoaderController extends AbstractController {

	protected abstract Logger getLogger();
	
	private ResourceLoader resourceLoader;
	private Date resourceLastModifiedDate;
	private String buildVersion;
	private boolean supportCaching = false;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		if (isSupportCaching()) {
	 		if (getLogger().isInfoEnabled()) {
	 			getLogger().info("isSupportCaching() is true. " +
	 					"Will handle HTTP caching related headers.");
	 		}
			return handleRequestInternalWithHttpClientCachingSupport(request,
					response);
		} else {
	 		if (getLogger().isInfoEnabled()) {
	 			getLogger().info("isSupportCaching() is false. " +
	 					"Will _not_ handle HTTP caching related headers.");
	 		}
			
			return doHandleRequestInternal(request, response);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private ModelAndView handleRequestInternalWithHttpClientCachingSupport(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// etag header support based on implementation at http://www.infoq.com/articles/etags 
		// Based on http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.3.4 we also handle
		// the Last-Modified-Since header. We cannot use Spring's org.springframework.web.servlet.mvc.LastModified
		// interface, since that would cause the Last-Modified-Since value to supercede the Etag value, violating
		// the HTTP spec.
	 	if (!isForceRefreshRequested(request) && isResponseUnmodifiedSinceLastRequest(request)) {
	 		if (getLogger().isDebugEnabled()) {
	 			getLogger().debug("Response not modified since last request: ");
	 			getLogger().debug("If-None-Match: " + getIfNoneMatchHeader(request));
	 			getLogger().debug("If-Modified-Since: " + getIfModifiedSinceHeader(request));
	 		}
	 		
	 		response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
	 		
	 		response.setDateHeader("Last-Modified", getResourceLastModifiedDate().getTime());
	 		response.setHeader("Etag", getBuildVersion());
	 		return null;
	 	} else {
	 		if (getLogger().isDebugEnabled()) {
		 		getLogger().debug("Response modified since last request. Returning full response.");
	 			getLogger().debug("If-None-Match: " + getIfNoneMatchHeader(request));
	 			getLogger().debug("If-Modified-Since: " + getIfModifiedSinceHeader(request));
	 		}
	 		
	 		response.setDateHeader("Last-Modified", getResourceLastModifiedDate().getTime());
	 		response.setHeader("Etag", getBuildVersion());
	 		
	 		return doHandleRequestInternal(request, response);
	 	}
	}

	private boolean isResponseUnmodifiedSinceLastRequest(
			HttpServletRequest request) {
		
		if (isIfNoneMatchHeaderAvailable(request) && isIfModifiedSinceHeaderAvailable(request)) {
			getLogger().debug("If-None-Match and If-Modified-Since headers both provided.");
			// Both need to match according to http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.3.4
			return previousEtagValueMatchesIfNoneMatchHeader(request) 
				&& previousModifiedValueMatchesIfModifiedSinceHeader(request);
		} else if (isIfNoneMatchHeaderAvailable(request)) {
			getLogger().debug("If-None-Match only provided.");
			return previousEtagValueMatchesIfNoneMatchHeader(request);
		} else if (isIfModifiedSinceHeaderAvailable(request)) {
			getLogger().debug("If-Modified-Since only provided.");
			return previousModifiedValueMatchesIfModifiedSinceHeader(request);
		} else {
			getLogger().debug("Neither If-None-Match or If-Modified-Since headers provided.");
			return false;
		}
	}
	
	/**
	 * @param request
	 * @return
	 */
	private boolean isForceRefreshRequested(HttpServletRequest request) {
		return "true".equals(request.getHeader("x-sensis-js-refresh"));
	}
	
	private boolean previousEtagValueMatchesIfNoneMatchHeader(
			HttpServletRequest request) {
	  	String previousEtagHeaderValue = getIfNoneMatchHeader(request);
	 	if (previousEtagHeaderValue != null && getBuildVersion().equals(previousEtagHeaderValue)) {
	 		getLogger().debug("If-None-Match matches.");
	 		return true;
	 	} else {
	 		getLogger().debug("If-None-Match does not match.");
	 		return false;
	 	}
	}

	/**
	 * @param request
	 * @return
	 */
	private String getIfNoneMatchHeader(HttpServletRequest request) {
		return request.getHeader("If-None-Match");
	}
	
	private boolean previousModifiedValueMatchesIfModifiedSinceHeader(
			HttpServletRequest request) {
		long previousLastModifiedHeaderValue = getIfModifiedSinceHeader(request);
		if (getResourceLastModifiedDate().getTime() == previousLastModifiedHeaderValue) {
			getLogger().debug("If-Modified-Since matches.");
			return true;
		} else {
			getLogger().debug("If-Modified-Since does not match.");
			return false;
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private long getIfModifiedSinceHeader(HttpServletRequest request) {
		return request.getDateHeader("If-Modified-Since");
	}

	private boolean isIfNoneMatchHeaderAvailable(HttpServletRequest request) {
		return getIfNoneMatchHeader(request) != null;
	}
	
	private boolean isIfModifiedSinceHeaderAvailable(HttpServletRequest request) {
		return request.getHeader("If-Modified-Since") != null;
	}

	protected abstract ModelAndView doHandleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * @param response
	 * @param javascriptBundleClasspath
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	protected final void writeInputStreamToResponse(HttpServletResponse response,
			InputStream inputStream) throws IOException {

		try {
			PrintWriter printWriter = response.getWriter();
			printWriter.println(IOUtils.toString(inputStream));
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * @param request
	 * @param requestUriPrefix
	 * @return
	 */
	protected final String extractFilenameRequested(HttpServletRequest request,
			String requestUriPrefix) {
		String filenameRequested =
				StringUtils.substringAfterLast(request.getRequestURI(),
						requestUriPrefix);
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("filenameRequested: '" + filenameRequested + "'");
		}
		return filenameRequested;
	}

	/**
	 * @return the resourceLoader
	 */
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
	
	/**
	 * @param resourceLastModifiedDate the resourceLastModifiedDate to set
	 */
	public void setResourceLastModifiedDate(Date resourceLastModifiedDate) {
		this.resourceLastModifiedDate = resourceLastModifiedDate;
	}

	/**
	 * @return the resourceLastModifiedDate
	 */
	public Date getResourceLastModifiedDate() {
		return resourceLastModifiedDate;
	}
	
	/**
	 * @return the buildVersion
	 */
	public String getBuildVersion() {
		return buildVersion;
	}

	/**
	 * @param buildVersion the buildVersion to set
	 */
	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	/**
	 * @param resourceLoader the resourceLoader to set
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * @return the supportCaching
	 */
	public boolean isSupportCaching() {
		return supportCaching;
	}

	/**
	 * @param supportCaching the supportCaching to set
	 */
	public void setSupportCaching(boolean supportCaching) {
		this.supportCaching = supportCaching;
	}

}
