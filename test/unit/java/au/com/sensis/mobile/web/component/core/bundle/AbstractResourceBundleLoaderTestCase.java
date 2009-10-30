package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;
import au.com.sensis.wireless.web.common.validation.ValidatableTestUtils;

/**
 * Base class for all unit tests of {@link AbstractResourceBundleLoader} subclasses.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public abstract class AbstractResourceBundleLoaderTestCase extends AbstractJUnit4TestCase {

    private static final String BUNDLE_BASE_PATH = "/js/bundles/";
    private static final String BUNDLE_EXPLODER_BASE_PATH = "/js/bundleExploders/";
    private static final String EXPLODED_BUNDLE_MEMBER_BASE_PATH = "/js/explodedBundles/";

    private static final long LAST_MODIFIED = 1234567890;

    private AbstractResourceBundleLoader objectUnderTest;
    private TestResourceBundleLoader localObjectUnderTest;
    private InputStream mockInputStream;
    private IOException ioException;
    private static final String BUNDLE_NAME = "my bundle name";

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public final void baseSetUp() throws Exception {
        objectUnderTest = createObjectUnderTest();

        objectUnderTest.setBundleBasePath(BUNDLE_BASE_PATH);
        objectUnderTest.setExplodedBundleMemberBasePath(EXPLODED_BUNDLE_MEMBER_BASE_PATH);
        objectUnderTest.setBundleExploderBasePath(BUNDLE_EXPLODER_BASE_PATH);

        localObjectUnderTest =
                new TestResourceBundleLoader(getMockInputStream(), LAST_MODIFIED, null);

        ioException = new IOException("test");
    }

    protected abstract AbstractResourceBundleLoader createObjectUnderTest();

    protected AbstractResourceBundleLoader getObjectUnderTest() {
        return objectUnderTest;
    }

    @Test
    public void testValidateStateWhenSuccessful() throws Throwable {
        getObjectUnderTest().validateState();
    }

    @Test
    public void testValidateStateWhenBundleBasePathIsBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setBundleBasePath(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "bundleBasePath");
        }
    }

    @Test
    public void testValidateStateWhenBundleExploderBasePathIsBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setBundleExploderBasePath(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "bundleExploderBasePath");
        }
    }

    @Test
    public void testValidateStateWhenExplodedBundleMemberBasePathIsBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setExplodedBundleMemberBasePath(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "explodedBundleMemberBasePath");
        }
    }

    @Test
    public void testGetBundlePath() throws Throwable {
        Assert.assertEquals("getBundlePath() is wrong",
                BUNDLE_BASE_PATH + BUNDLE_NAME, getObjectUnderTest()
                        .getBundlePath(BUNDLE_NAME));
    }

    @Test
    public void testGetBundleExploderPath() throws Throwable {
        Assert.assertEquals("getBundleExploderPath() is wrong",
                BUNDLE_EXPLODER_BASE_PATH + BUNDLE_NAME, getObjectUnderTest()
                        .getBundleExploderPath(BUNDLE_NAME));
    }

    @Test
    public void testGetExplodedBundleMemberPath() throws Throwable {
        Assert.assertEquals("getExplodedBundleMemberPath() is wrong",
                EXPLODED_BUNDLE_MEMBER_BASE_PATH + BUNDLE_NAME,
                getObjectUnderTest().getExplodedBundleMemberPath(BUNDLE_NAME));
    }

    @Test
    public void testLoadBundle() throws Throwable {
        getLocalObjectUnderTest().setBundleBasePath(BUNDLE_BASE_PATH);

        final InputStream actualInputStream =
                getLocalObjectUnderTest().loadBundle(BUNDLE_NAME);

        Assert.assertEquals(
                "loadFile method was not delegated to with correct file path",
                BUNDLE_BASE_PATH + BUNDLE_NAME, getLocalObjectUnderTest()
                        .getActualLoadFilePath());
        Assert.assertSame("Wrong InputStream returned", mockInputStream,
                actualInputStream);
    }

    @Test
    public void testLoadBundleExploder() throws Throwable {
        getLocalObjectUnderTest().setBundleExploderBasePath(BUNDLE_EXPLODER_BASE_PATH);

        final InputStream actualInputStream =
            getLocalObjectUnderTest().loadBundleExploder(BUNDLE_NAME);

        Assert.assertEquals(
                "loadFile method was not delegated to with correct file path",
                BUNDLE_EXPLODER_BASE_PATH + BUNDLE_NAME, getLocalObjectUnderTest()
                .getActualLoadFilePath());
        Assert.assertSame("Wrong InputStream returned", mockInputStream,
                actualInputStream);
    }

    @Test
    public void testLoadExplodedBundleMember() throws Throwable {
        getLocalObjectUnderTest().setExplodedBundleMemberBasePath(EXPLODED_BUNDLE_MEMBER_BASE_PATH);

        final InputStream actualInputStream =
            getLocalObjectUnderTest().loadExplodedBundleMember(BUNDLE_NAME);

        Assert.assertEquals(
                "loadFile method was not delegated to with correct file path",
                EXPLODED_BUNDLE_MEMBER_BASE_PATH + BUNDLE_NAME, getLocalObjectUnderTest()
                .getActualLoadFilePath());
        Assert.assertSame("Wrong InputStream returned", mockInputStream,
                actualInputStream);
    }

    @Test
    public void testGetBundleLastModified() throws Throwable {
        getLocalObjectUnderTest().setBundleBasePath(BUNDLE_BASE_PATH);

        final long  actualLastModified =
                getLocalObjectUnderTest().getBundleLastModified(BUNDLE_NAME);

        Assert.assertEquals(
                "getFileLastModified method was not delegated to with correct file path",
                BUNDLE_BASE_PATH + BUNDLE_NAME, getLocalObjectUnderTest()
                        .getActualLoadFilePath());
        Assert.assertEquals("Last modified is wrong", LAST_MODIFIED,
                actualLastModified);
    }

    @Test
    public void testGetBundleLastModifiedWhenExceptionThrown() throws Throwable {
        localObjectUnderTest =
                new TestResourceBundleLoader(getMockInputStream(),
                        LAST_MODIFIED, ioException);

        getLocalObjectUnderTest().setBundleBasePath(BUNDLE_BASE_PATH);

        try {
            getLocalObjectUnderTest().getBundleLastModified(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertSame("IOException is wrong instance",
                    getIoException(), e);
        }
    }

    @Test
    public void testGetBundleExploderLastModified() throws Throwable {
        getLocalObjectUnderTest().setBundleExploderBasePath(BUNDLE_EXPLODER_BASE_PATH);

        final long  actualLastModified =
            getLocalObjectUnderTest().getBundleExploderLastModified(BUNDLE_NAME);

        Assert.assertEquals(
                "getFileLastModified method was not delegated to with correct file path",
                BUNDLE_EXPLODER_BASE_PATH + BUNDLE_NAME, getLocalObjectUnderTest()
                .getActualLoadFilePath());
        Assert.assertEquals("Last modified is wrong", LAST_MODIFIED,
                actualLastModified);
    }

    @Test
    public void testGetBundleExploderLastModifiedWhenExceptionThrown() throws Throwable {
        localObjectUnderTest =
            new TestResourceBundleLoader(getMockInputStream(),
                    LAST_MODIFIED, ioException);

        getLocalObjectUnderTest().setBundleExploderBasePath(BUNDLE_EXPLODER_BASE_PATH);

        try {
            getLocalObjectUnderTest().getBundleExploderLastModified(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertSame("IOException is wrong instance",
                    getIoException(), e);
        }
    }

    @Test
    public void testGetExplodedBundleMemberLastModified() throws Throwable {
        getLocalObjectUnderTest().setExplodedBundleMemberBasePath(EXPLODED_BUNDLE_MEMBER_BASE_PATH);

        final long  actualLastModified =
                getLocalObjectUnderTest().getExplodedBundleMemberLastModified(BUNDLE_NAME);

        Assert.assertEquals(
                "getFileLastModified method was not delegated to with correct file path",
                EXPLODED_BUNDLE_MEMBER_BASE_PATH + BUNDLE_NAME, getLocalObjectUnderTest()
                        .getActualLoadFilePath());
        Assert.assertEquals("Last modified is wrong", LAST_MODIFIED,
                actualLastModified);
    }

    @Test
    public void testGetExplodedBundleMemberLastModifiedWhenExceptionThrown() throws Throwable {
        localObjectUnderTest =
                new TestResourceBundleLoader(getMockInputStream(),
                        LAST_MODIFIED, ioException);

        getLocalObjectUnderTest().setExplodedBundleMemberBasePath(EXPLODED_BUNDLE_MEMBER_BASE_PATH);

        try {
            getLocalObjectUnderTest().getExplodedBundleMemberLastModified(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertSame("IOException is wrong instance",
                    getIoException(), e);
        }
    }


    /**
     * @return the mockInputStream
     */
    public InputStream getMockInputStream() {
        return mockInputStream;
    }

    /**
     * @param mockInputStream the mockInputStream to set
     */
    public void setMockInputStream(final InputStream mockInputStream) {
        this.mockInputStream = mockInputStream;
    }

    /**
     * @return the localObjectUnderTest
     */
    public TestResourceBundleLoader getLocalObjectUnderTest() {
        return localObjectUnderTest;
    }

    /**
     * @return the ioException
     */
    public IOException getIoException() {
        return ioException;
    }

    /**
     * {@link AbstractResourceBundleLoader} implementation so that we can detect
     * delegation to the {@link AbstractResourceBundleLoader#loadFile(String)}
     * method.
     */
    private static final class TestResourceBundleLoader extends
            AbstractResourceBundleLoader {

        private String actualLoadFilePath;

        private final InputStream loadFileReturnValue;
        private final long lastModifiedReturnValue;
        private final IOException ioExceptionToThrow;

        private TestResourceBundleLoader(final InputStream loadFileReturnValue,
                final long lastModifiedReturnValue, final IOException ioExceptionToThrow) {
            super();
            this.loadFileReturnValue = loadFileReturnValue;
            this.lastModifiedReturnValue = lastModifiedReturnValue;
            this.ioExceptionToThrow = ioExceptionToThrow;
        }

        @Override
        protected InputStream loadFile(final String filePath) {
            actualLoadFilePath = filePath;
            return loadFileReturnValue;
        }

        /**
         * @return the actualLoadFilePath
         */
        private String getActualLoadFilePath() {
            return actualLoadFilePath;
        }

        @Override
        protected long getFileLastModified(final String filePath)
                throws IOException {
            if (ioExceptionToThrow != null) {
                throw ioExceptionToThrow;
            } else {
                actualLoadFilePath = filePath;
                return lastModifiedReturnValue;
            }
        }

    }
}
