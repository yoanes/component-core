package au.com.sensis.mobile.web.component.core.bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link ChainedResourceBundleLoader}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ChainedResourceBundleLoaderTestCase extends AbstractJUnit4TestCase {

    private static final String BUNDLE_NAME = "myBundle";
    private static final String LOADER_FAILED_WILL_TRY_NEXT_MSG =
            "ResourceBundleLoader at index 0 threw an exception for requested bundle '"
                    + BUNDLE_NAME
                    + "'. Will try next ResourceBundleLoader in the chain.";
    private static final String LAST_LOADER_FAILED =
            "The last ResourceBundleLoader in the chain (index 1) threw an exception "
                    + "for requested bundle '" + BUNDLE_NAME + "'. Aborting.";

    private static final long EXPECTED_TIME_STAMP = 3874;

    private ChainedResourceBundleLoader objectUnderTest;
    private ResourceBundleLoader mockResourceBundleLoader1;
    private ResourceBundleLoader mockResourceBundleLoader2;
    private IOException expectedException;
    private InputStream mockInputStream;

    /**
     * Setup test data.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        final List<ResourceBundleLoader> resourceBundleLoaders =
                new ArrayList<ResourceBundleLoader>();
        resourceBundleLoaders.add(getMockResourceBundleLoader1());
        resourceBundleLoaders.add(getMockResourceBundleLoader2());
        setObjectUnderTest(new ChainedResourceBundleLoader(
                resourceBundleLoaders));

        swapOutRealLoggerForMock(ChainedResourceBundleLoader.class);

        setExpectedException(new IOException("test"));
    }

    @Test
    public void testConstructorWithNullArg() throws Throwable {
        try {
            new ChainedResourceBundleLoader(null);
            Assert.fail("IllegalArgumentException expected");
        } catch (final IllegalArgumentException e) {
            Assert.assertEquals("IllegalArgumentException has wrong message",
                    "resourceBundleLoaders must be non-empty. Was: null", e
                            .getMessage());
        }

    }

    @Test
    public void testConstructorWithEmptyListArg() throws Throwable {
        final ArrayList<ResourceBundleLoader> resourceBundleLoaders =
                new ArrayList<ResourceBundleLoader>();
        try {
            new ChainedResourceBundleLoader(resourceBundleLoaders);
            Assert.fail("IllegalArgumentException expected");
        } catch (final IllegalArgumentException e) {
            Assert.assertEquals("IllegalArgumentException has wrong message",
                    "resourceBundleLoaders must be non-empty. Was: "
                            + resourceBundleLoaders, e.getMessage());
        }
    }

    @Test
    public void testGetBundleExploderLastModifiedWhenFirstInChainSuccess()
            throws Throwable {
        recordeGetBundleExploderLastModifiedSuccess(getMockResourceBundleLoader1());

        replay();

        final long actualResult =
                getObjectUnderTest().getBundleExploderLastModified(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", EXPECTED_TIME_STAMP,
                actualResult);
    }

    @Test
    public void testGetBundleExploderLastModifiedWhenSecondInChainSuccess()
            throws Throwable {

        recordeGetBundleExploderLastModifiedFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeGetBundleExploderLastModifiedSuccess(getMockResourceBundleLoader2());

        replay();

        final long actualResult =
                getObjectUnderTest().getBundleExploderLastModified(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", EXPECTED_TIME_STAMP,
                actualResult);

    }

    @Test
    public void testGetBundleExploderLastModifiedWhenAllInChainFail()
            throws Throwable {
        recordeGetBundleExploderLastModifiedFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeGetBundleExploderLastModifiedFailed(getMockResourceBundleLoader2());

        replay();

        try {
            getObjectUnderTest().getBundleExploderLastModified(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    LAST_LOADER_FAILED, e.getMessage());
            Assert.assertEquals("IOException has wrong cause",
                    getExpectedException(), e.getCause());
        }
    }

    @Test
    public void testGetBundleLastModifiedWhenFirstInChainSuccess()
            throws Throwable {
        recordeGetBundleLastModifiedSuccess(getMockResourceBundleLoader1());

        replay();

        final long actualResult =
                getObjectUnderTest().getBundleLastModified(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", EXPECTED_TIME_STAMP,
                actualResult);
    }

    @Test
    public void testGetBundleLastModifiedWhenSecondInChainSuccess()
    throws Throwable {

        recordeGetBundleLastModifiedFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeGetBundleLastModifiedSuccess(getMockResourceBundleLoader2());

        replay();

        final long actualResult =
            getObjectUnderTest().getBundleLastModified(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", EXPECTED_TIME_STAMP,
                actualResult);

    }

    @Test
    public void testGetBundleLastModifiedWhenAllInChainFail()
    throws Throwable {
        recordeGetBundleLastModifiedFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeGetBundleLastModifiedFailed(getMockResourceBundleLoader2());

        replay();

        try {
            getObjectUnderTest().getBundleLastModified(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    LAST_LOADER_FAILED, e.getMessage());
            Assert.assertEquals("IOException has wrong cause",
                    getExpectedException(), e.getCause());
        }
    }

    @Test
    public void testGetExplodedBundleMemberLastModifiedWhenFirstInChainSuccess()
    throws Throwable {
        recordeGetExplodedBundleMemberLastModifiedSuccess(getMockResourceBundleLoader1());

        replay();

        final long actualResult =
            getObjectUnderTest().getExplodedBundleMemberLastModified(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", EXPECTED_TIME_STAMP,
                actualResult);
    }

    @Test
    public void testGetExplodedBundleMemberLastModifiedWhenSecondInChainSuccess()
    throws Throwable {

        recordeGetExplodedBundleMemberLastModifiedFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeGetExplodedBundleMemberLastModifiedSuccess(getMockResourceBundleLoader2());

        replay();

        final long actualResult =
            getObjectUnderTest().getExplodedBundleMemberLastModified(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", EXPECTED_TIME_STAMP,
                actualResult);

    }

    @Test
    public void testGetExplodedBundleMemberLastModifiedWhenAllInChainFail()
    throws Throwable {
        recordeGetExplodedBundleMemberLastModifiedFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeGetExplodedBundleMemberLastModifiedFailed(getMockResourceBundleLoader2());

        replay();

        try {
            getObjectUnderTest().getExplodedBundleMemberLastModified(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    LAST_LOADER_FAILED, e.getMessage());
            Assert.assertEquals("IOException has wrong cause",
                    getExpectedException(), e.getCause());
        }
    }

    @Test
    public void testLoadBundleWhenFirstInChainSuccess() throws Throwable {
        recordeLoadBundleSuccess(getMockResourceBundleLoader1());

        replay();

        final InputStream actualResult =
            getObjectUnderTest().loadBundle(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", getMockInputStream(),
                actualResult);
    }

    @Test
    public void testLoadBundleWhenSecondInChainSuccess()
        throws Throwable {

        recordeLoadBundleFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeLoadBundleSuccess(getMockResourceBundleLoader2());

        replay();

        final InputStream actualResult =
            getObjectUnderTest().loadBundle(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", getMockInputStream(),
                actualResult);

    }

    @Test
    public void testLoadBundleWhenAllInChainFail()
        throws Throwable {
        recordeLoadBundleFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeLoadBundleFailed(getMockResourceBundleLoader2());

        replay();

        try {
            getObjectUnderTest().loadBundle(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    LAST_LOADER_FAILED, e.getMessage());
            Assert.assertEquals("IOException has wrong cause",
                    getExpectedException(), e.getCause());
        }
    }

    @Test
    public void testLoadBundleExploderWhenFirstInChainSuccess() throws Throwable {
        recordeLoadBundleExploderSuccess(getMockResourceBundleLoader1());

        replay();

        final InputStream actualResult =
                getObjectUnderTest().loadBundleExploder(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", getMockInputStream(),
                actualResult);
    }

    @Test
    public void testLoadBundleExploderWhenSecondInChainSuccess()
        throws Throwable {

        recordeLoadBundleExploderFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeLoadBundleExploderSuccess(getMockResourceBundleLoader2());

        replay();

        final InputStream actualResult =
            getObjectUnderTest().loadBundleExploder(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", getMockInputStream(),
                actualResult);

    }

    @Test
    public void testLoadBundleExploderWhenAllInChainFail()
        throws Throwable {
        recordeLoadBundleExploderFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeLoadBundleExploderFailed(getMockResourceBundleLoader2());

        replay();

        try {
            getObjectUnderTest().loadBundleExploder(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    LAST_LOADER_FAILED, e.getMessage());
            Assert.assertEquals("IOException has wrong cause",
                    getExpectedException(), e.getCause());
        }
    }

    @Test
    public void testLoadExplodedBundleMemberWhenFirstInChainSuccess() throws Throwable {
        recordeLoadExplodedBundleMemberSuccess(getMockResourceBundleLoader1());

        replay();

        final InputStream actualResult =
            getObjectUnderTest().loadExplodedBundleMember(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", getMockInputStream(),
                actualResult);
    }

    @Test
    public void testLoadExplodedBundleMemberWhenSecondInChainSuccess()
        throws Throwable {

        recordeLoadExplodedBundleMemberFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeLoadExplodedBundleMemberSuccess(getMockResourceBundleLoader2());

        replay();

        final InputStream actualResult =
            getObjectUnderTest().loadExplodedBundleMember(BUNDLE_NAME);

        Assert.assertEquals("actualResult is wrong", getMockInputStream(),
                actualResult);

    }

    @Test
    public void testLoadExplodedBundleMemberWhenAllInChainFail()
        throws Throwable {
        recordeLoadExplodedBundleMemberFailed(getMockResourceBundleLoader1());

        recordResourceBundleLoaderFailedWillTryNextLogMsg();

        recordeLoadExplodedBundleMemberFailed(getMockResourceBundleLoader2());

        replay();

        try {
            getObjectUnderTest().loadExplodedBundleMember(BUNDLE_NAME);
            Assert.fail("IOException expected");
        } catch (final IOException e) {
            Assert.assertEquals("IOException has wrong message",
                    LAST_LOADER_FAILED, e.getMessage());
            Assert.assertEquals("IOException has wrong cause",
                    getExpectedException(), e.getCause());
        }
    }

    /**

     * @return the objectUnderTest
     */
    public ChainedResourceBundleLoader getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest
     *            the objectUnderTest to set
     */
    public void setObjectUnderTest(
            final ChainedResourceBundleLoader objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the mockResourceBundleLoader1
     */
    public ResourceBundleLoader getMockResourceBundleLoader1() {
        return mockResourceBundleLoader1;
    }

    /**
     * @param mockResourceBundleLoader1
     *            the mockResourceBundleLoader1 to set
     */
    public void setMockResourceBundleLoader1(
            final ResourceBundleLoader mockResourceBundleLoader1) {
        this.mockResourceBundleLoader1 = mockResourceBundleLoader1;
    }

    /**
     * @return the mockResourceBundleLoader2
     */
    public ResourceBundleLoader getMockResourceBundleLoader2() {
        return mockResourceBundleLoader2;
    }

    /**
     * @param mockResourceBundleLoader2
     *            the mockResourceBundleLoader2 to set
     */
    public void setMockResourceBundleLoader2(
            final ResourceBundleLoader mockResourceBundleLoader2) {
        this.mockResourceBundleLoader2 = mockResourceBundleLoader2;
    }

    /**
     * @param expectedException
     *            the expectedException to set
     */
    private void setExpectedException(final IOException expectedException) {
        this.expectedException = expectedException;
    }

    /**
     * @return the expectedException
     */
    private IOException getExpectedException() {
        return expectedException;
    }

    private void recordResourceBundleLoaderFailedWillTryNextLogMsg() {
        EasyMock.expect(
                getMockLogger(ChainedResourceBundleLoader.class).isEnabledFor(
                        Level.WARN)).andReturn(Boolean.TRUE).atLeastOnce();
        getMockLogger(ChainedResourceBundleLoader.class).warn(
                LOADER_FAILED_WILL_TRY_NEXT_MSG, getExpectedException());
    }

    private void recordeGetBundleExploderLastModifiedSuccess(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock
                .expect(
                        resourceBundleLoader
                                .getBundleExploderLastModified(BUNDLE_NAME))
                .andReturn(EXPECTED_TIME_STAMP);
    }

    private void recordeGetBundleExploderLastModifiedFailed(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock
                .expect(
                        resourceBundleLoader
                                .getBundleExploderLastModified(BUNDLE_NAME))
                .andThrow(getExpectedException());

    }

    private void recordeGetBundleLastModifiedSuccess(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock
        .expect(
                resourceBundleLoader
                .getBundleLastModified(BUNDLE_NAME))
                .andReturn(EXPECTED_TIME_STAMP);
    }

    private void recordeGetBundleLastModifiedFailed(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock
        .expect(
                resourceBundleLoader
                .getBundleLastModified(BUNDLE_NAME))
                .andThrow(getExpectedException());

    }

    private void recordeGetExplodedBundleMemberLastModifiedSuccess(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock
                .expect(resourceBundleLoader.getExplodedBundleMemberLastModified(BUNDLE_NAME))
                .andReturn(EXPECTED_TIME_STAMP);
    }

    private void recordeGetExplodedBundleMemberLastModifiedFailed(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock
                .expect(resourceBundleLoader.getExplodedBundleMemberLastModified(BUNDLE_NAME))
                .andThrow(getExpectedException());

    }

    private void recordeLoadBundleSuccess(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock.expect(resourceBundleLoader.loadBundle(BUNDLE_NAME))
                .andReturn(getMockInputStream());
    }

    private void recordeLoadBundleFailed(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock.expect(resourceBundleLoader.loadBundle(BUNDLE_NAME)).andThrow(
                getExpectedException());

    }

    private void recordeLoadBundleExploderSuccess(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock.expect(resourceBundleLoader.loadBundleExploder(BUNDLE_NAME))
        .andReturn(getMockInputStream());
    }

    private void recordeLoadBundleExploderFailed(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock.expect(resourceBundleLoader.loadBundleExploder(BUNDLE_NAME)).andThrow(
                getExpectedException());

    }
    private void recordeLoadExplodedBundleMemberSuccess(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock.expect(resourceBundleLoader.loadExplodedBundleMember(BUNDLE_NAME))
        .andReturn(getMockInputStream());
    }

    private void recordeLoadExplodedBundleMemberFailed(
            final ResourceBundleLoader resourceBundleLoader) throws IOException {
        EasyMock.expect(resourceBundleLoader.loadExplodedBundleMember(BUNDLE_NAME)).andThrow(
                getExpectedException());

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


}
