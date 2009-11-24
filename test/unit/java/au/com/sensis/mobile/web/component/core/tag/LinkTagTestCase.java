package au.com.sensis.mobile.web.component.core.tag;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockJspWriter;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link LinkTag}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class LinkTagTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_HREF = "/map/map.mthm";

    private static final String DYN_ATTR1_URI = "http://www.w3.org/2002/06/xhtml2";

    private static final String DYN_ATTR1_LOCALNAME = "rel";

    private static final Object DYN_ATTR1_VALUE = "mcs:theme";

    private LinkTag objectUnderTest;
    private JspContext mockJspContext;
    private JspWriter mockJspWriter;
    private StringWriter stringWriter;
    private Map<String, LinkTagWriter> linkTagWriterMap;

    /**
     * Test setup.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new LinkTag());

        stringWriter = new StringWriter();
        setMockJspWriter(new MockJspWriter(stringWriter));

        linkTagWriterMap = new HashMap<String, LinkTagWriter>();
    }

    @Test
    public void testDoTag() throws Throwable {
        getObjectUnderTest().setDynamicAttribute(DYN_ATTR1_URI,
                DYN_ATTR1_LOCALNAME, DYN_ATTR1_VALUE);
        getObjectUnderTest().setHref(DEFAULT_HREF);

        getObjectUnderTest().setJspContext(getMockJspContext());

        EasyMock.expect(
                getMockJspContext().getAttribute(
                        LinkTag.LINK_WRITER_MAP_ATTRIBUTE_NAME,
                        PageContext.REQUEST_SCOPE)).andReturn(null);
        getMockJspContext()
                .setAttribute(LinkTag.LINK_WRITER_MAP_ATTRIBUTE_NAME,
                        new HashMap<String, LinkTagWriter>(),
                        PageContext.REQUEST_SCOPE);
        EasyMock.expect(
                getMockJspContext().getAttribute(
                        LinkTag.LINK_WRITER_MAP_ATTRIBUTE_NAME,
                        PageContext.REQUEST_SCOPE)).andReturn(linkTagWriterMap).atLeastOnce();

        EasyMock.expect(getMockJspContext().getOut()).andReturn(
                getMockJspWriter());

        replay();

        getObjectUnderTest().doTag();

        Assert.assertEquals("incorrect string written by tag", "<link href=\""
                + DEFAULT_HREF + "\" " + DYN_ATTR1_LOCALNAME + "=\""
                + DYN_ATTR1_VALUE + "\" />", stringWriter.getBuffer()
                .toString());
        Assert.assertTrue("linkTagWriterMap doesn't contain entry for "
                + DEFAULT_HREF, linkTagWriterMap.containsKey(DEFAULT_HREF));
    }

    @Test
    public void testDoTagWhenDuplicate() throws Throwable {
        final Map<String, LinkTagWriter> linkTagWriterMap =
                new HashMap<String, LinkTagWriter>();
        linkTagWriterMap.put(DEFAULT_HREF, new SimpleLinkTagWriter(null,
                DEFAULT_HREF));

        getObjectUnderTest().setDynamicAttribute(DYN_ATTR1_URI,
                DYN_ATTR1_LOCALNAME, DYN_ATTR1_VALUE);
        getObjectUnderTest().setHref(DEFAULT_HREF);

        getObjectUnderTest().setJspContext(getMockJspContext());

        EasyMock.expect(
                getMockJspContext().getAttribute(
                        LinkTag.LINK_WRITER_MAP_ATTRIBUTE_NAME,
                        PageContext.REQUEST_SCOPE)).andReturn(linkTagWriterMap)
                .atLeastOnce();

        replay();

        getObjectUnderTest().doTag();

        Assert.assertEquals("incorrect string written by tag",
                StringUtils.EMPTY, stringWriter.getBuffer().toString());
    }

    /**
     * @return the objectUnderTest
     */
    public LinkTag getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    public void setObjectUnderTest(final LinkTag objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the mockJspContext
     */
    public JspContext getMockJspContext() {
        return mockJspContext;
    }

    /**
     * @param mockJspContext the mockJspContext to set
     */
    public void setMockJspContext(final JspContext mockJspContext) {
        this.mockJspContext = mockJspContext;
    }

    /**
     * @return the mockJspWriter
     */
    public JspWriter getMockJspWriter() {
        return mockJspWriter;
    }

    /**
     * @param mockJspWriter the mockJspWriter to set
     */
    public void setMockJspWriter(final JspWriter mockJspWriter) {
        this.mockJspWriter = mockJspWriter;
    }

    /**
     * @return the linkTagWriterMap
     */
    public Map<String, LinkTagWriter> getLinkTagWriterMap() {
        return linkTagWriterMap;
    }

    /**
     * @param linkTagWriterMap the linkTagWriterMap to set
     */
    public void setLinkTagWriterMap(final Map<String, LinkTagWriter> linkTagWriterMap) {
        this.linkTagWriterMap = linkTagWriterMap;
    }

    @Override
    public void verify() {
        // Override to prevent auto verify.
    }
}
