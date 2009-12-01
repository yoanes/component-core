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

    private static final long LAST_MODIFIED = 1234567890;

    private AbstractResourceBundleLoader objectUnderTest;
    private TestResourceBundleLoader localObjectUnderTest;
    private InputStream mockInputStream;
    private IOException ioException;

    private static final String BUNDLE_NAME_REGEX = "(.*?)/js/(.*)";
    private static final String BUNDLE_NAME_REPLACEMENT = "/js/bundles/$1/$2";
    private static final String BUNDLE_EXPLODER_NAME_REPLACEMENT
        = "/js/bundleExploders/$1/$2";
    private static final String EXPLODED_BUNDLE_MEMBER_NAME_REGEX = "(.*?)/jsmember/(.*)";
    private static final String EXPLODED_BUNDLE_MEMBER_NAME_REPLACEMENT
        = "/js/explodedBundles/$2";

    private static final String BUNDLE_NAME
        = "core/js/core-component-default.js";
    private static final String EXPLODED_BUNDLE_MEMBER_NAME
        = "core/jsmember/core/device-invariant/mootools.js";

    private static final String EXPECTED_BUNDLE_LOAD_FILE_PATH
        = "/js/bundles/core/core-component-default.js";
    private static final String EXPECTED_BUNDLE_EXPLODER_LOAD_FILE_PATH
        = "/js/bundleExploders/core/core-component-default.js";
    private static final String EXPECTED_EXPLODED_BUNDLE_MEMBER_LOAD_FILE_PATH
        = "/js/explodedBundles/core/device-invariant/mootools.js";

    /**
     * Setup test data.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public final void baseSetUp() throws Exception {
        objectUnderTest = createObjectUnderTest();
        init(getObjectUnderTest());

        localObjectUnderTest =
                new TestResourceBundleLoader(getMockInputStream(),
                        LAST_MODIFIED);
        init(getLocalObjectUnderTest());

        ioException = new IOException("test");
    }

    /**
     *
     */
    private void init(final AbstractResourceBundleLoader abstractResourceBundleLoader) {
        abstractResourceBundleLoader.setBundleNameRegex(BUNDLE_NAME_REGEX);
        abstractResourceBundleLoader.setBundleNameReplacement(BUNDLE_NAME_REPLACEMENT);
        abstractResourceBundleLoader
                .setBundleExploderNameReplacement(BUNDLE_EXPLODER_NAME_REPLACEMENT);
        abstractResourceBundleLoader
            .setExplodedBundleMemberNameRegex(EXPLODED_BUNDLE_MEMBER_NAME_REGEX);
        abstractResourceBundleLoader
                .setExplodedBundleMemberNameReplacement(EXPLODED_BUNDLE_MEMBER_NAME_REPLACEMENT);
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
    public void testValidateStateWhenBundleNameRegexBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setBundleNameRegex(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "bundleNameRegex");
        }
    }

    @Test
    public void testValidateStateWhenBundleNameReplacementIsBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setBundleNameReplacement(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "bundleNameReplacement");
        }
    }

    @Test
    public void testValidateStateWhenBundleExploderNameReplacementIsBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setBundleExploderNameReplacement(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "bundleExploderNameReplacement");
        }
    }

    @Test
    public void testValidateStateWhenExplodedBundleMemberNameRegexBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setExplodedBundleMemberNameRegex(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "explodedBundleMemberNameRegex");
        }
    }

    @Test
    public void testValidateStateWhenExplodedBundleMemberNameReplacementIsBlank() throws Throwable {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };
        for (final String testValue : testValues) {
            getObjectUnderTest().setExplodedBundleMemberNameReplacement(testValue);
            ValidatableTestUtils.testValidateStateWithCustomFailureMessage(
                    "Current testValue: '" + testValue + "'.",
                    getObjectUnderTest(), "explodedBundleMemberNameReplacement");
        }
    }

    @Test
    public void testGetBundlePathWhenBundleNameIsNull() throws Throwable {
        Assert.assertEquals("getBundlePath() is wrong",
                StringUtils.EMPTY, getObjectUnderTest()
                        .getBundlePath(null));
    }

    @Test
    public void testGetBundleExploderPathWhenBundleNameIsNull() throws Throwable {
        Assert.assertEquals("getBundleExploderPath() is wrong",
                StringUtils.EMPTY, getObjectUnderTest()
                        .getBundleExploderPath(null));
    }

    @Test
    public void testGetExplodedBundleMemberPathWhenExplodedBundleMemberNameIsNull()
        throws Throwable {
        Assert.assertEquals("getExplodedBundleMemberPath() is wrong",
                StringUtils.EMPTY,
                getObjectUnderTest().getExplodedBundleMemberPath(null));
    }

    @Test
    public void testLoadBundle() throws Throwable {
        final InputStream actualInputStream =
                getLocalObjectUnderTest().loadBundle(BUNDLE_NAME);

        Assert.assertEquals(
                "loadFile method was not delegated to with correct file path",
                EXPECTED_BUNDLE_LOAD_FILE_PATH, getLocalObjectUnderTest()
                        .getActualLoadFilePath());
        Assert.assertSame("Wrong InputStream returned", mockInputStream,
                actualInputStream);
    }

    @Test
    public void testLoadBundleExploder() throws Throwable {

        final InputStream actualInputStream =
            getLocalObjectUnderTest().loadBundleExploder(BUNDLE_NAME);

        Assert.assertEquals(
                "loadFile method was not delegated to with correct file path",
                EXPECTED_BUNDLE_EXPLODER_LOAD_FILE_PATH, getLocalObjectUnderTest()
                .getActualLoadFilePath());
        Assert.assertSame("Wrong InputStream returned", mockInputStream,
                actualInputStream);
    }

    @Test
    public void testLoadExplodedBundleMember() throws Throwable {
        final InputStream actualInputStream =
            getLocalObjectUnderTest().loadExplodedBundleMember(EXPLODED_BUNDLE_MEMBER_NAME);

        Assert.assertEquals(
                "loadFile method was not delegated to with correct file path",
                EXPECTED_EXPLODED_BUNDLE_MEMBER_LOAD_FILE_PATH, getLocalObjectUnderTest()
                .getActualLoadFilePath());
        Assert.assertSame("Wrong InputStream returned", mockInputStream,
                actualInputStream);
    }

    @Test
    public void testGetBundleLastModified() throws Throwable {
        final long  actualLastModified =
                getLocalObjectUnderTest().getBundleLastModified(BUNDLE_NAME);

        Assert.assertEquals(
                "getFileLastModified method was not delegated to with correct file path",
                EXPECTED_BUNDLE_LOAD_FILE_PATH, getLocalObjectUnderTest()
                        .getActualLoadFilePath());
        Assert.assertEquals("Last modified is wrong", LAST_MODIFIED,
                actualLastModified);
    }

    @Test
    public void testGetBundleLastModifiedWhenExceptionThrown() throws Throwable {
        getLocalObjectUnderTest().setIoExceptionToThrow(ioException);

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
        final long  actualLastModified =
            getLocalObjectUnderTest().getBundleExploderLastModified(BUNDLE_NAME);

        Assert.assertEquals(
                "getFileLastModified method was not delegated to with correct file path",
                EXPECTED_BUNDLE_EXPLODER_LOAD_FILE_PATH, getLocalObjectUnderTest()
                .getActualLoadFilePath());
        Assert.assertEquals("Last modified is wrong", LAST_MODIFIED,
                actualLastModified);
    }

    @Test
    public void testGetBundleExploderLastModifiedWhenExceptionThrown() throws Throwable {
        getLocalObjectUnderTest().setIoExceptionToThrow(ioException);

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
        final long  actualLastModified =
                getLocalObjectUnderTest().getExplodedBundleMemberLastModified(
                        EXPLODED_BUNDLE_MEMBER_NAME);

        Assert.assertEquals(
                "getFileLastModified method was not delegated to with correct file path",
                EXPECTED_EXPLODED_BUNDLE_MEMBER_LOAD_FILE_PATH, getLocalObjectUnderTest()
                        .getActualLoadFilePath());
        Assert.assertEquals("Last modified is wrong", LAST_MODIFIED,
                actualLastModified);
    }

    @Test
    public void testGetExplodedBundleMemberLastModifiedWhenExceptionThrown() throws Throwable {
        getLocalObjectUnderTest().setIoExceptionToThrow(ioException);

        try {
            getLocalObjectUnderTest().getExplodedBundleMemberLastModified(
                    EXPLODED_BUNDLE_MEMBER_NAME);
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
        private IOException ioExceptionToThrow;

        private TestResourceBundleLoader(final InputStream loadFileReturnValue,
                final long lastModifiedReturnValue) {
            super();
            this.loadFileReturnValue = loadFileReturnValue;
            this.lastModifiedReturnValue = lastModifiedReturnValue;
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

        /**
         * @param ioExceptionToThrow the ioExceptionToThrow to set
         */
        private void setIoExceptionToThrow(
                final IOException ioExceptionToThrow) {
            this.ioExceptionToThrow = ioExceptionToThrow;
        }

    }
}
