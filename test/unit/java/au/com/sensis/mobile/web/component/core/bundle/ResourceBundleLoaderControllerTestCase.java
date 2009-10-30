package au.com.sensis.mobile.web.component.core.bundle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import junitx.util.PrivateAccessor;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;


/**
 * Unit test {@link ResourceBundleLoaderController}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ResourceBundleLoaderControllerTestCase
    extends AbstractResourceBundleLoaderControllerTestCase {

    @Override
    protected ResourceBundleLoaderController createObjectUnderTest() {
        return new ResourceBundleLoaderController();
    }

    @Override
    protected ResourceBundleLoaderController getObjectUnderTest() {
        return (ResourceBundleLoaderController) super.getObjectUnderTest();
    }

    @Test
    public void testHandleRequestInternalWhenBundleExploderRequested()
            throws Throwable {
        getSimpleFeatureEnablementRegistryBean().setBundleExplosionEnabled(true);
        getObjectUnderTest().setContextRootComponentPrefix("/myapp/");
        recordBundleExploderRequested();

        final String resourceName = recordExtractResourceNameRequested();
        final InputStream resourceLoaded =
                new ByteArrayInputStream("resource contents".getBytes());
        EasyMock.expect(
                getMockResourceBundleLoader().loadBundleExploder(resourceName))
                .andReturn(resourceLoaded);

        replay();

        final MockHttpServletResponse springMockHttpServletResponse =
                new MockHttpServletResponse();
        getObjectUnderTest().handleRequestInternal(getMockHttpServletRequest(),
                springMockHttpServletResponse);

        Assert.assertTrue("Wrong content written to response. Response was: '"
                + springMockHttpServletResponse.getContentAsString() + "'",
                springMockHttpServletResponse.getContentAsString().matches(
                        "var jsPathPrefix='/myapp/';\\s*resource contents\\s*"));
    }

    @Test
    public void testHandleRequestInternalWhenBundleExploderNotRequested() throws Throwable {
        getSimpleFeatureEnablementRegistryBean().setBundleExplosionEnabled(false);
        getObjectUnderTest().setContextRootComponentPrefix("/myapp/");

        final String resourceName = recordExtractResourceNameRequested();

        final InputStream resourceLoaded =
            new ByteArrayInputStream("resource contents".getBytes());
        EasyMock.expect(
                getMockResourceBundleLoader().loadBundle(resourceName))
                .andReturn(resourceLoaded);

        replay();

        final MockHttpServletResponse springMockHttpServletResponse =
                new MockHttpServletResponse();
        getObjectUnderTest().handleRequestInternal(getMockHttpServletRequest(),
                springMockHttpServletResponse);

        Assert.assertTrue("Wrong content written to response. Response was: '"
                + springMockHttpServletResponse.getContentAsString() + "'",
                springMockHttpServletResponse.getContentAsString().matches(
                        "\\s*resource contents\\s*"));

    }

    @Test
    public void testIsBundleExploderRequested() throws Throwable {
        for (final BundleExploderRequestedTestData testData : BundleExploderRequestedTestData
                .getIsBundleExploderRequestedTestData()) {

            getSimpleFeatureEnablementRegistryBean().setBundleExplosionEnabled(
                    testData.isAllowBundleExploderRequestParam());

            if (testData.isAllowBundleExploderRequestParam()) {
                if (testData.isBundleExploderRequestParamSet()) {
                    recordBundleExploderRequested();
                } else {
                    recordBundleExploderNotRequested();
                }
            }

            replay();

            final Boolean actualOutcome = (Boolean) PrivateAccessor.invoke(getObjectUnderTest(),
                    "isBundleExploderRequested", new Class [] {HttpServletRequest.class},
                    new Object [] {getMockHttpServletRequest()});

            Assert.assertEquals("isBundleExploderRequested returned wrong value for testData: "
                    + testData, testData.isExpectedOutcome(), actualOutcome);

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);
        }
    }

    private void recordBundleExploderNotRequested() {
        EasyMock.expect(
                getMockHttpServletRequest().getSession()).andReturn(getMockHttpSession());
        EasyMock.expect(
                getMockHttpSession().getAttribute(
                        ResourceBundleLoaderController.BUNDLE_EXPLODER_REQUEST_SESSION_KEY))
                .andReturn(Boolean.FALSE).atLeastOnce();
    }

    private void recordBundleExploderRequested() {
        EasyMock.expect(
                getMockHttpServletRequest().getSession()).andReturn(getMockHttpSession());
        EasyMock.expect(
                getMockHttpSession().getAttribute(
                        ResourceBundleLoaderController.BUNDLE_EXPLODER_REQUEST_SESSION_KEY))
                .andReturn(Boolean.TRUE).atLeastOnce();
    }

    @Test
    public void testGetLastModified() throws Throwable {
        for (final LastModifiedTestData testData : LastModifiedTestData.getLastModifiedTestData()) {
            if (testData.isBypassClientCacheRequested()) {
                getSimpleFeatureEnablementRegistryBean().setBypassClientCacheEnabled(true);
                recordBypassClientCacheRequested();
            } else {
                getSimpleFeatureEnablementRegistryBean().setBypassClientCacheEnabled(false);
            }

            final String resourceName = recordExtractResourceNameRequested();

            if (testData.isBundleExploderRequested()) {
                getSimpleFeatureEnablementRegistryBean().setBundleExplosionEnabled(true);
                recordBundleExploderRequested();

                if (!testData.isBypassClientCacheRequested()) {
                    EasyMock.expect(getMockResourceBundleLoader().getBundleExploderLastModified(
                            resourceName)).andReturn(testData.getExpectedOutcome());
                }

            } else {
                getSimpleFeatureEnablementRegistryBean().setBundleExplosionEnabled(false);

                if (!testData.isBypassClientCacheRequested()) {
                    EasyMock.expect(getMockResourceBundleLoader().getBundleLastModified(
                            resourceName)).andReturn(testData.getExpectedOutcome());
                }
            }

            replay();

            Assert.assertEquals("lastModified is wrong for testData: " + testData,
                    testData.getExpectedOutcome(),
                    getObjectUnderTest().getLastModified(getMockHttpServletRequest()));

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);

        }

    }

    private static final class BundleExploderRequestedTestData {
        private final boolean allowBundleExploderRequestParam;
        private final boolean bundleExploderRequestParamSet;
        private final boolean expectedOutcome;

        private BundleExploderRequestedTestData(
                final boolean allowBundleExploderRequestParam,
                final boolean bundleExploderRequestParamSet,
                final boolean expectedOutcome) {
            this.allowBundleExploderRequestParam =
                    allowBundleExploderRequestParam;
            this.bundleExploderRequestParamSet = bundleExploderRequestParamSet;
            this.expectedOutcome = expectedOutcome;
        }

        private static BundleExploderRequestedTestData
            [] getIsBundleExploderRequestedTestData() {
            return new BundleExploderRequestedTestData [] {
                    new BundleExploderRequestedTestData(true, true, true),
                    new BundleExploderRequestedTestData(true, false, false),
                    new BundleExploderRequestedTestData(false, true, false),
                    new BundleExploderRequestedTestData(false, false, false)
            };
        }

        /**
         * @return the allowBundleExploderRequestParam
         */
        public boolean isAllowBundleExploderRequestParam() {
            return allowBundleExploderRequestParam;
        }


        /**
         * @return the expectedOutcome
         */
        public boolean isExpectedOutcome() {
            return expectedOutcome;
        }

        /**
         * @return the bundleExploderRequestParamSet
         */
        public boolean isBundleExploderRequestParamSet() {
            return bundleExploderRequestParamSet;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                .append(isAllowBundleExploderRequestParam())
                .append(isBundleExploderRequestParamSet())
                .append(isExpectedOutcome()).toString();
        }
    }
    private static final class LastModifiedTestData {

        private static final long LAST_MODIFIED = 10039375;
        private final boolean bypassClientCacheRequested;
        private final boolean bundleExploderRequested;
        private final long expectedOutcome;


        private LastModifiedTestData(final boolean bypassClientCacheRequested,
                final boolean bundleExploderRequested, final long expectedOutcome) {
            super();
            this.bypassClientCacheRequested = bypassClientCacheRequested;
            this.bundleExploderRequested = bundleExploderRequested;
            this.expectedOutcome = expectedOutcome;
        }

        private static LastModifiedTestData
            [] getLastModifiedTestData() {
            return new LastModifiedTestData [] {
                    new LastModifiedTestData(true, true, -1),
                    new LastModifiedTestData(true, false, -1),
                    new LastModifiedTestData(false, true, LAST_MODIFIED),
                    new LastModifiedTestData(false, false, LAST_MODIFIED)
            };
        }

        /**
         * @return the expectedOutcome
         */
        public long getExpectedOutcome() {
            return expectedOutcome;
        }

        /**
         * @return the bypassClientCacheRequested
         */
        public boolean isBypassClientCacheRequested() {
            return bypassClientCacheRequested;
        }

        /**
         * @return the bundleExploderRequested
         */
        public boolean isBundleExploderRequested() {
            return bundleExploderRequested;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
            .append(getExpectedOutcome()).toString();
        }
    }
}
