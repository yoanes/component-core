package au.com.sensis.mobile.web.component.core.bundle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;


/**
 * Unit test {@link ExplodedBundleMemberResourceLoaderController}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ExplodedBundleMemberResourceLoaderControllerTestCase
    extends AbstractResourceBundleLoaderControllerTestCase {

    @Override
    protected ExplodedBundleMemberResourceLoaderController createObjectUnderTest() {
        return new ExplodedBundleMemberResourceLoaderController();
    }

    @Override
    protected ExplodedBundleMemberResourceLoaderController getObjectUnderTest() {
        return (ExplodedBundleMemberResourceLoaderController) super.getObjectUnderTest();
    }

    @Test
    public void testHandleRequestInternal()
            throws Throwable {
        final String resourceName = recordExtractResourceNameRequested();
        final InputStream resourceLoaded =
                new ByteArrayInputStream("resource contents".getBytes());
        EasyMock.expect(
                getMockResourceBundleLoader().loadExplodedBundleMember(resourceName))
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
    public void testGetLastModified() throws Throwable {

        for (final LastModifiedTestData testData : LastModifiedTestData.getLastModifiedTestData()) {
            if (testData.isBypassClientCacheRequested()) {
                getSimpleFeatureEnablementRegistryBean().setBypassClientCacheEnabled(true);
                recordBypassClientCacheRequested();

            } else {
                getSimpleFeatureEnablementRegistryBean().setBypassClientCacheEnabled(false);

                final String resourceName = recordExtractResourceNameRequested();
                EasyMock.expect(getMockResourceBundleLoader().getExplodedBundleMemberLastModified(
                        resourceName)).andReturn(testData.getExpectedOutcome());
            }


            replay();

            Assert.assertEquals("lastModified is wrong for testData: " + testData,
                    testData.getExpectedOutcome(),
                    getObjectUnderTest().getLastModified(getMockHttpServletRequest()));

            // Explicitly call verify since we are in a loop and can't rely on the inherited,
            // automated verify call.
            verify();

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);

        }
    }

    private static final class LastModifiedTestData {

        private static final long LAST_MODIFIED = 10039375;
        private final boolean bypassClientCacheRequested;
        private final long expectedOutcome;


        private LastModifiedTestData(final boolean bypassClientCacheRequested,
                final long expectedOutcome) {
            super();
            this.bypassClientCacheRequested = bypassClientCacheRequested;
            this.expectedOutcome = expectedOutcome;
        }

        private static LastModifiedTestData
            [] getLastModifiedTestData() {
            return new LastModifiedTestData [] {
                    new LastModifiedTestData(true, -1),
                    new LastModifiedTestData(false, LAST_MODIFIED)
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

        @Override
        public String toString() {
            return new ToStringBuilder(this)
            .append(getExpectedOutcome()).toString();
        }
    }
}
