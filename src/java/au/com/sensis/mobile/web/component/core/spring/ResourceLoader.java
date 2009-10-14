package au.com.sensis.mobile.web.component.core.spring;

import java.io.InputStream;

public interface ResourceLoader {

	InputStream loadJavaScriptBundle(String bundleName);

	InputStream loadJavaScriptBundleExplodedFilesLoader(String bundleName);

	InputStream loadSingleExlpodedJavaScriptFile(
			String explodedJavaScriptFilename);

}