package au.com.sensis.mobile.web.component.core.tag;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.jsp.JspWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockJspWriter;

import au.com.sensis.mobile.crf.presentation.tag.DynamicTagAttribute;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link LinkTagWriterWriter}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class LinkTagWriterTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_HREF = "/map/map.mthm";

    private static final String DYN_ATTR1_URI = "http://www.w3.org/2002/06/xhtml2";
    private static final String DYN_ATTR1_LOCALNAME = "rel";
    private static final Object DYN_ATTR1_VALUE = "mcs:theme";
    private static final String DYN_ATTR2_URI = "http://www.w3.org/2002/06/xhtml2-2";
    private static final String DYN_ATTR2_LOCALNAME = "rel-2";
    private static final Object DYN_ATTR2_VALUE = "mcs:theme-2";

    private LinkTagWriter objectUnderTest;
    private JspWriter mockJspWriter;
    private StringWriter stringWriter;

    private DynamicTagAttribute dynamicTagAttribute1;

    private DynamicTagAttribute dynamicTagAttribute2;

    /**
     * Test setup.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        dynamicTagAttribute1 = new DynamicTagAttribute(DYN_ATTR1_URI, DYN_ATTR1_LOCALNAME,
                DYN_ATTR1_VALUE);
        dynamicTagAttribute2 = new DynamicTagAttribute(DYN_ATTR2_URI, DYN_ATTR2_LOCALNAME,
                DYN_ATTR2_VALUE);
        setObjectUnderTest(new SimpleLinkTagWriter(Arrays
                .asList(dynamicTagAttribute1), DEFAULT_HREF));

        stringWriter = new StringWriter();
        setMockJspWriter(new MockJspWriter(stringWriter));
    }

    @Test
    public void testDoTagWhenNoDynamicAttributes() throws Throwable {
        setObjectUnderTest(new SimpleLinkTagWriter(
                new ArrayList<DynamicTagAttribute>(), DEFAULT_HREF));

        replay();

        getObjectUnderTest().writeTag(getMockJspWriter());

        Assert.assertEquals("incorrect string written by tag", "<link href=\""
                + DEFAULT_HREF + "\" " + "/>", stringWriter.getBuffer()
                .toString());
    }

    @Test
    public void testDoTagWhenOneDynamicAttribute() throws Throwable {
        replay();

        getObjectUnderTest().writeTag(getMockJspWriter());

        Assert.assertEquals("incorrect string written by tag", "<link href=\""
                + DEFAULT_HREF + "\" " + DYN_ATTR1_LOCALNAME + "=\""
                + DYN_ATTR1_VALUE + "\" />", stringWriter.getBuffer()
                .toString());
    }

    @Test
    public void testDoTagWhenMoreThanOneDynamicAttribute() throws Throwable {
        setObjectUnderTest(new SimpleLinkTagWriter(Arrays.asList(
                dynamicTagAttribute1, dynamicTagAttribute2), DEFAULT_HREF));

        replay();

        getObjectUnderTest().writeTag(getMockJspWriter());

        Assert.assertEquals("incorrect string written by tag", "<link href=\""
                + DEFAULT_HREF + "\" " + DYN_ATTR1_LOCALNAME + "=\""
                + DYN_ATTR1_VALUE + "\" " + DYN_ATTR2_LOCALNAME + "=\""
                + DYN_ATTR2_VALUE + "\" />", stringWriter.getBuffer()
                .toString());
    }

    /**
     * @return the objectUnderTest
     */
    public LinkTagWriter getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    public void setObjectUnderTest(final LinkTagWriter objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
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

}
