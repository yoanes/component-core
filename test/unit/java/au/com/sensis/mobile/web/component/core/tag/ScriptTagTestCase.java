package au.com.sensis.mobile.web.component.core.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import junitx.util.PrivateAccessor;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.mobile.web.component.core.tag.ScriptTag.McsScriptBeanFactory;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link ScriptTag}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class ScriptTagTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_SRC = "/comp/map/scripts/map-component.mscr";
    private static final String DEFAULT_NAME = "deviceInfo";
    private static final String DEFAULT_TYPE = "text/javascript";

    private ScriptTag objectUnderTest;

    private JspContext mockJspContext;
    private JspWriter mockJspWriter;
    private JspFragment mockJspFragment;
    private McsScriptBean mockMcsScriptBean;
    private McsScriptBeanFactory mockMcsScriptBeanFactory;

    private Map<String, McsScriptBean> mcsScriptBeanMap;

    /**
     * Test setup.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        objectUnderTest = new ScriptTag();
        objectUnderTest.setJspContext(getMockJspContext());
        objectUnderTest.setJspBody(getMockJspFragment());
        objectUnderTest.setSrc(DEFAULT_SRC);
        objectUnderTest.setName(DEFAULT_NAME);
        objectUnderTest.setType(DEFAULT_TYPE);

        objectUnderTest.setMcsScriptBeanFactory(getMockMcsScriptBeanFactory());

        mcsScriptBeanMap = new HashMap<String, McsScriptBean>();
    }

    @Test
    public void testInitMcsScriptBeanMapInJspContextIfRequiredWhenExistingIsNull()
        throws Throwable {
        recordGetScriptMcsScriptBeanMapFromPageContext(null);

        getMockJspContext().setAttribute(
                EasyMock.eq(ScriptTag.MCS_SCRIPT_BEAN_MAP_ATTRIBUTE_NAME),
                EasyMock.isA(HashMap.class),
                EasyMock.eq(PageContext.REQUEST_SCOPE));

        getHelper().replay();

        PrivateAccessor.invoke(objectUnderTest,
                "initMcsScriptBeanMapInJspContextIfRequired", null, null);
    }

    @Test
    public void testInitMcsScriptBeanMapInJspContextIfRequiredWhenExistingIsNotNull()
        throws Throwable {
        recordGetScriptMcsScriptBeanMapFromPageContext(mcsScriptBeanMap);

        getHelper().replay();

        PrivateAccessor.invoke(objectUnderTest,
                "initMcsScriptBeanMapInJspContextIfRequired", null, null);
    }

    @Test
    public void testGetMcsScriptBeanMapFromPageContext() throws Throwable {
        recordGetScriptMcsScriptBeanMapFromPageContext(mcsScriptBeanMap);

        getHelper().replay();

        Assert.assertSame("map is wrong instance", mcsScriptBeanMap, PrivateAccessor
                .invoke(objectUnderTest, "getMcsScriptBeanMapFromJspContext",
                        null, null));
    }

    private void recordGetScriptMcsScriptBeanMapFromPageContext(
            final Map<String, McsScriptBean> mcsScriptBeanMapToReturn) {
        EasyMock.expect(
                getMockJspContext().getAttribute(
                        ScriptTag.MCS_SCRIPT_BEAN_MAP_ATTRIBUTE_NAME,
                        PageContext.REQUEST_SCOPE)).andReturn(mcsScriptBeanMapToReturn)
                .atLeastOnce();
    }

    /**
     * Test {@link ScriptTag#doTag()}.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Test
    public void testDoTagWhenMcsScriptBeanMapExistsInJspContextButDoesNotContainMcsScriptBean()
            throws Exception {
        recordGetScriptMcsScriptBeanMapFromPageContext(mcsScriptBeanMap);

        EasyMock.expect(
                getMockMcsScriptBeanFactory().createMcsScriptBean(DEFAULT_SRC,
                        DEFAULT_NAME, DEFAULT_TYPE)).andReturn(
                getMockMcsScriptBean());

        EasyMock.expect(getMockMcsScriptBean().getId()).andReturn(DEFAULT_SRC).atLeastOnce();

        EasyMock.expect(getMockJspContext().getOut()).andReturn(getMockJspWriter()).atLeastOnce();

        getMockMcsScriptBean().writeMcsScript(getMockJspWriter(), getMockJspFragment());

        getHelper().replay();

        objectUnderTest.doTag();

        Assert.assertSame("mcsScriptBeanMap does not contain correct instance",
                getMockMcsScriptBean(), mcsScriptBeanMap.get(DEFAULT_SRC));
    }

    /**
     * Test {@link ScriptTag#doTag()}.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Test
    public void testDoTagWhenMcsScriptBeanMapExistsInJspContextAndContainMcsScriptBean()
        throws Exception {
        mcsScriptBeanMap.put(DEFAULT_SRC, getMockMcsScriptBean());

        recordGetScriptMcsScriptBeanMapFromPageContext(mcsScriptBeanMap);

        EasyMock.expect(
                getMockMcsScriptBeanFactory().createMcsScriptBean(DEFAULT_SRC,
                        DEFAULT_NAME, DEFAULT_TYPE)).andReturn(
                                getMockMcsScriptBean());

        EasyMock.expect(getMockMcsScriptBean().getId()).andReturn(DEFAULT_SRC).atLeastOnce();

        getHelper().replay();

        objectUnderTest.doTag();

        Assert.assertSame("mcsScriptBeanMap does not contain correct instance",
                getMockMcsScriptBean(), mcsScriptBeanMap.get(DEFAULT_SRC));
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
     * @return the mockMcsScriptBean
     */
    public McsScriptBean getMockMcsScriptBean() {
        return mockMcsScriptBean;
    }

    /**
     * @param mockMcsScriptBean the mockMcsScriptBean to set
     */
    public void setMockMcsScriptBean(final McsScriptBean mockMcsScriptBean) {
        this.mockMcsScriptBean = mockMcsScriptBean;
    }

    /**
     * @return the mockMcsScriptBeanFactory
     */
    public McsScriptBeanFactory getMockMcsScriptBeanFactory() {
        return mockMcsScriptBeanFactory;
    }

    /**
     * @param mockMcsScriptBeanFactory the mockMcsScriptBeanFactory to set
     */
    public void setMockMcsScriptBeanFactory(
            final McsScriptBeanFactory mockMcsScriptBeanFactory) {
        this.mockMcsScriptBeanFactory = mockMcsScriptBeanFactory;
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
