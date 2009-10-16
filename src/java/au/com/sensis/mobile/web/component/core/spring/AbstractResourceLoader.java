package au.com.sensis.mobile.web.component.core.spring;

import java.io.InputStream;

public abstract class AbstractResourceLoader implements ResourceLoader {

	private static final String JAVASCRIPT_BASE_PATH = "/js/";
	private static final String JAVASCRIPT_BUNDLES_BASE_PATH = JAVASCRIPT_BASE_PATH + "bundles/";
	private static final String JAVASCRIPT_EXPLODED_LOADER_BASE_PATH =
			JAVASCRIPT_BASE_PATH + "exploded-loaders/";
	private static final String JAVASCRIPT_EXPLODED_JAVASCRIPT_BASE_PATH =
			JAVASCRIPT_BASE_PATH + "exploded/";

	public AbstractResourceLoader() {
		super();
	}

	/**
	 * @param bundleName
	 * @return
	 */
	protected final String getBundlePath(final String bundleName) {
		return JAVASCRIPT_BUNDLES_BASE_PATH + bundleName;
	}

	/**
	 * @param bundleName
	 * @return
	 */
	protected final String getBundleExplodedFilesLoaderPath(final String bundleName) {
		return JAVASCRIPT_EXPLODED_LOADER_BASE_PATH + bundleName;
	}

	/**
	 * @param explodedJavaScriptFilename
	 * @return
	 */
	protected final String getSingleExplodedJavaScriptFilePath(final String explodedJavaScriptFilename) {
		return JAVASCRIPT_EXPLODED_JAVASCRIPT_BASE_PATH + explodedJavaScriptFilename;
	}

	/* (non-Javadoc)
	 * @see au.com.sensis.mobile.web.component.core.spring.ResourceLoader#loadJavaScriptBundle(java.lang.String)
	 */
	public InputStream loadJavaScriptBundle(final String bundleName) {
		final String bundlePath = getBundlePath(bundleName);
		return loadFile(bundlePath);
	}

	/* (non-Javadoc)
	 * @see au.com.sensis.mobile.web.component.core.spring.ResourceLoader#loadJavaScriptBundleExplodedFilesLoader(java.lang.String)
	 */
	public InputStream loadJavaScriptBundleExplodedFilesLoader(final String bundleName) {
		final String bundleOnClasspath = getBundleExplodedFilesLoaderPath(bundleName);
		return loadFile(bundleOnClasspath);
	}

	/* (non-Javadoc)
	 * @see au.com.sensis.mobile.web.component.core.spring.ResourceLoader#loadSingleExlpodedJavaScriptFile(java.lang.String)
	 */
	public InputStream loadSingleExlpodedJavaScriptFile(final String explodedJavaScriptFilename) {
		final String explodedJavaScriptFilenameOnClasspath = getSingleExplodedJavaScriptFilePath(explodedJavaScriptFilename);
		return loadFile(explodedJavaScriptFilenameOnClasspath);

	}

	protected abstract InputStream loadFile(String filePath);


}