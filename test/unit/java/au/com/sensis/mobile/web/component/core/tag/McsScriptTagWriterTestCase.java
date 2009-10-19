package au.com.sensis.mobile.web.component.core.tag;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link McsScriptTagWriter}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class McsScriptTagWriterTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_SRC = "/comp/map/scripts/map-component.mscr";
    private static final String DEFAULT_NAME = "deviceInfo";
    private static final String DEFAULT_TYPE = "text/javascript";

    private JspWriter mockJspWriter;
    private JspFragment mockJspFragment;

    /**
     * Test {@link McsScriptTagWriter#getId()}.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Test
    public void testGetIdWhenSrcIsNotBlank() throws Exception {
       final McsScriptTagWriter objectUnderTest
           = new McsScriptTagWriter(DEFAULT_SRC, DEFAULT_NAME, DEFAULT_TYPE);

       Assert.assertEquals("id is wrong", DEFAULT_SRC, objectUnderTest.getId());
    }

    /**
     * Test {@link McsScriptTagWriter#getId()}.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Test
    public void testGetIdWhenSrcIsBlank() throws Exception {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };

        for (final String testValue : testValues) {
            final McsScriptTagWriter objectUnderTest =
                    new McsScriptTagWriter(testValue, DEFAULT_NAME, DEFAULT_TYPE);
            Assert.assertEquals("id is wrong for testValue: '" + testValue
                    + "'", DEFAULT_NAME, objectUnderTest.getId());
        }
    }

    /**
     * Test {@link McsScriptTagWriter#writeScript(javax.servlet.jsp.JspWriter,
     * javax.servlet.jsp.tagext.JspFragment)}.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Test
    public void testWriteMcsScriptWhenSrcAndTypeAreNotBlank() throws Exception {
       final McsScriptTagWriter objectUnderTest
           = new McsScriptTagWriter(DEFAULT_SRC, DEFAULT_NAME, DEFAULT_TYPE);

       getMockJspWriter().println("<mcs:script ");
       getMockJspWriter().println("type=\"" + DEFAULT_TYPE + "\" ");
       getMockJspWriter().println("src=\"" + DEFAULT_SRC + "\"/>");
       getMockJspFragment().invoke(mockJspWriter);
       getMockJspWriter().println("</mcs:script>");

       getHelper().replay();

       objectUnderTest.writeScript(getMockJspWriter(), getMockJspFragment());
    }

    /**
     * Test
     * {@link McsScriptTagWriter#writeScript(javax.servlet.jsp.JspWriter,
     * javax.servlet.jsp.tagext.JspFragment)}.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Test
    public void testWriteMcsScriptWhenSrcIsNotBlankAndTypeIsBlank()
            throws Exception {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };

        for (final String testValue : testValues) {

            final McsScriptTagWriter objectUnderTest =
                    new McsScriptTagWriter(DEFAULT_SRC, DEFAULT_NAME, testValue);

            getMockJspWriter().println("<mcs:script ");
            getMockJspWriter().println("src=\"" + DEFAULT_SRC + "\"/>");
            getMockJspFragment().invoke(mockJspWriter);
            getMockJspWriter().println("</mcs:script>");

            getHelper().replay();

            objectUnderTest.writeScript(getMockJspWriter(),
                    getMockJspFragment());

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);
        }
    }

    /**
     * Test
     * {@link McsScriptTagWriter#writeScript(javax.servlet.jsp.JspWriter,
     * javax.servlet.jsp.tagext.JspFragment)}.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Test
    public void testWriteMcsScriptWhenSrcIsBlankAndTypeIsNotBlank()
            throws Exception {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };

        for (final String testValue : testValues) {

            final McsScriptTagWriter objectUnderTest =
                    new McsScriptTagWriter(testValue, DEFAULT_NAME, DEFAULT_TYPE);

            getMockJspWriter().println("<mcs:script ");
            getMockJspWriter().println("type=\"" + DEFAULT_TYPE + "\" ");
            getMockJspWriter().println(">");
            getMockJspFragment().invoke(mockJspWriter);
            getMockJspWriter().println("</mcs:script>");

            getHelper().replay();

            objectUnderTest.writeScript(getMockJspWriter(),
                    getMockJspFragment());

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);
        }
    }

    /**
     * Test
     * {@link McsScriptTagWriter#writeScript(javax.servlet.jsp.JspWriter,
     * javax.servlet.jsp.tagext.JspFragment)}.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Test
    public void testWriteMcsScriptWhenSrcAndTypeAreBlank()
            throws Exception {
        final String[] testValues = { null, StringUtils.EMPTY, " ", "  " };

        for (final String testValue : testValues) {

            final McsScriptTagWriter objectUnderTest =
                    new McsScriptTagWriter(testValue, DEFAULT_NAME, testValue);

            getMockJspWriter().println("<mcs:script ");
            getMockJspWriter().println(">");
            getMockJspFragment().invoke(mockJspWriter);
            getMockJspWriter().println("</mcs:script>");

            getHelper().replay();

            objectUnderTest.writeScript(getMockJspWriter(),
                    getMockJspFragment());

            // Reset mocks prior to next iteration.
            getHelper().reset();
            setReplayed(false);
        }
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
     * @return the mockJspFragment
     */
    public JspFragment getMockJspFragment() {
        return mockJspFragment;
    }

    /**
     * @param mockJspFragment the mockJspFragment to set
     */
    public void setMockJspFragment(final JspFragment mockJspFragment) {
        this.mockJspFragment = mockJspFragment;
    }
}
