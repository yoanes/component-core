package au.com.sensis.mobile.web.component.core.spring;

import org.junit.Test;

/**
 * @author Adrian.Koh2@sensis.com.au
 *
 * TODO: should extend standard base class. At the moment, this test case is just created to test the build scripts. 
 */
public class ClasspathResourceLoaderTestCase {

	// TODO: dummy test just to test build script creation of emma code coverage report. 
	@Test
	public void dummyTest() {
		ClasspathResourceLoader classpathResourceLoader = new ClasspathResourceLoader();
		classpathResourceLoader.loadFile("/au/com/sensis/mobile/web/component/core/spring/ClasspathResourceLoaderTestCase.class");
	}
}
