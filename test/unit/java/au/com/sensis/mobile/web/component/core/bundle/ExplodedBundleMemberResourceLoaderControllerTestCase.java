package au.com.sensis.mobile.web.component.core.bundle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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

    private static final long LAST_MODIFIED = 10039375;

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

        final String resourceName = recordExtractResourceNameRequested();
        EasyMock.expect(
                getMockResourceBundleLoader()
                        .getExplodedBundleMemberLastModified(resourceName))
                .andReturn(LAST_MODIFIED);

        replay();

        Assert.assertEquals("lastModified is wrong", LAST_MODIFIED,
                getObjectUnderTest().getLastModified(
                        getMockHttpServletRequest()));
    }
}
