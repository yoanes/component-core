package au.com.sensis.mobile.web.component.core.spring;

import java.io.InputStream;

import org.apache.log4j.Logger;

public class ClasspathResourceLoader extends AbstractResourceLoader implements ResourceLoader {
	
	private Logger logger = Logger.getLogger(ClasspathResourceLoader.class);
	
	@Override
	protected InputStream loadFile(String classpathFilename) {
		InputStream loadedFileInputStream =
				getClass().getResourceAsStream(classpathFilename);
		if (logger.isDebugEnabled()) {
			logger.debug("loadedFileInputStream: '"
					+ loadedFileInputStream + "'");
		}
		
		if (loadedFileInputStream != null) {
			return loadedFileInputStream;
		} else {
			throw new RuntimeException("Failed to load file from classpath: " + classpathFilename);
		}
	}
}
