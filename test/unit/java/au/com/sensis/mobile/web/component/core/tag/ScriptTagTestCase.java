package au.com.sensis.mobile.web.component.core.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.ValidationMessage;

import junitx.util.PrivateAccessor;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.mobile.web.component.core.tag.ScriptTag.ScriptTagExtraInfo;
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
    private ScriptTagExtraInfo scriptTagExtraInfoUnderTest;

    private JspContext mockJspContext;
    private JspWriter mockJspWriter;
    private JspFragment mockJspFragment;
    private ScriptTagWriter mockScriptTagWriter;
    private ScriptTagWriterFactory mockScriptTagWriterFactory;
    private TagData mockTagData;

    private Map<String, ScriptTagWriter> scriptTagWriterMap;

    /**
     * Test setup.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new ScriptTag());
        getObjectUnderTest().setJspContext(getMockJspContext());
        getObjectUnderTest().setJspBody(getMockJspFragment());
        getObjectUnderTest().setSrc(DEFAULT_SRC);
        getObjectUnderTest().setName(DEFAULT_NAME);
        getObjectUnderTest().setType(DEFAULT_TYPE);
        getObjectUnderTest().setScriptTagWriterFactory(getMockScriptTagWriterFactory());

        setScriptTagWriterMap(new HashMap<String, ScriptTagWriter>());

        setScriptTagExtraInfoUnderTest(new ScriptTagExtraInfo());
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

        recordGetScriptTagWriterMapFromJspContext(getScriptTagWriterMap());

        recordCreateScriptTagWriter();

        EasyMock.expect(getMockScriptTagWriter().getId()).andReturn(DEFAULT_SRC).atLeastOnce();

        recordWriteOutMcsScriptBean();

        getHelper().replay();

        getObjectUnderTest().doTag();

        Assert.assertSame("scriptTagWriterMap does not contain correct instance",
                getMockScriptTagWriter(), getScriptTagWriterMap().get(DEFAULT_SRC));
    }

    /**
     * @throws IOException
     * @throws JspException
     */
    private void recordWriteOutMcsScriptBean() throws IOException, JspException {
        EasyMock.expect(getMockJspContext().getOut()).andReturn(getMockJspWriter()).atLeastOnce();

        getMockScriptTagWriter().writeScript(getMockJspWriter(), getMockJspFragment());
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
        getScriptTagWriterMap().put(DEFAULT_SRC, getMockScriptTagWriter());

        recordGetScriptTagWriterMapFromJspContext(getScriptTagWriterMap());

        recordCreateScriptTagWriter();

        EasyMock.expect(getMockScriptTagWriter().getId()).andReturn(DEFAULT_SRC).atLeastOnce();

        getHelper().replay();

        getObjectUnderTest().doTag();

        Assert.assertSame("scriptTagWriterMap does not contain correct instance",
                getMockScriptTagWriter(), getScriptTagWriterMap().get(DEFAULT_SRC));
    }

    /**
     * Test {@link ScriptTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testScriptTagExtraInfoValidateWhenSrcAndNameAttributesAreBothNull()
            throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("src")).andReturn(null);
        EasyMock.expect(getMockTagData().getAttribute("name")).andReturn(null);

        EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getScriptTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertEquals("Number of validation messages is wrong", 1,
                actualValidationMessages.length);

        final ValidationMessage actualValidationMessage = actualValidationMessages[0];
        final ValidationMessage expectedValidationMessage =
                new ValidationMessage("tagDataId",
                        "You must set either the src or name attributes "
                                + "but not both. src='null'; name='null'");
        assertValidationMessagesEqual(expectedValidationMessage, actualValidationMessage);
    }

    /**
     * @param actualValidationMessage
     * @param expectedValidationMessage
     */
    private void assertValidationMessagesEqual(
            final ValidationMessage expectedValidationMessage,
            final ValidationMessage actualValidationMessage) {
        Assert.assertEquals("ValidationMessage has wrong id",
                expectedValidationMessage.getId(), actualValidationMessage
                        .getId());
        Assert.assertEquals("ValidationMessage has wrong message",
                expectedValidationMessage.getMessage(), actualValidationMessage
                        .getMessage());
    }

    /**
     * Test {@link ScriptTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testScriptTagExtraInfoValidateWhenSrcAndNameAttributesAreBothNotNull()
            throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("src")).andReturn("src value");
        EasyMock.expect(getMockTagData().getAttribute("name")).andReturn("name value");

        EasyMock.expect(getMockTagData().getId()).andReturn("tagDataId");

        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
                getScriptTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertEquals("Number of validation messages is wrong", 1,
                actualValidationMessages.length);

        final ValidationMessage actualValidationMessage = actualValidationMessages[0];
        final ValidationMessage expectedValidationMessage =
                new ValidationMessage("tagDataId",
                        "You must set either the src or name attributes "
                                + "but not both. src='src value'; name='name value'");
        assertValidationMessagesEqual(expectedValidationMessage, actualValidationMessage);
    }

    /**
     * Test {@link ScriptTagExtraInfo#validate(TagData)}.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testScriptTagExtraInfoValidateWhenValid()
        throws Throwable {
        EasyMock.expect(getMockTagData().getAttribute("src")).andReturn("src value");
        EasyMock.expect(getMockTagData().getAttribute("name")).andReturn(null);


        getHelper().replay();

        final ValidationMessage[] actualValidationMessages =
            getScriptTagExtraInfoUnderTest().validate(getMockTagData());

        Assert.assertNull("validate returned wrong value",
                actualValidationMessages);
    }

    @Test
    public void testInitMcsScriptBeanMapInJspContextIfRequiredWhenExistingIsNull()
        throws Throwable {
        recordGetScriptTagWriterMapFromJspContext(null);

        getMockJspContext().setAttribute(
                EasyMock.eq(ScriptTag.SCRIPT_WRITER_MAP_ATTRIBUTE_NAME),
                EasyMock.isA(HashMap.class),
                EasyMock.eq(PageContext.REQUEST_SCOPE));

        getHelper().replay();

        PrivateAccessor.invoke(getObjectUnderTest(),
                "initScriptWriterMapInJspContextIfRequired", null, null);
    }

    @Test
    public void testInitMcsScriptBeanMapInJspContextIfRequiredWhenExistingIsNotNull()
        throws Throwable {
        recordGetScriptTagWriterMapFromJspContext(getScriptTagWriterMap());

        getHelper().replay();

        PrivateAccessor.invoke(getObjectUnderTest(),
                "initScriptWriterMapInJspContextIfRequired", null, null);
    }

    @Test
    public void testGetMcsScriptBeanMapFromPageContext() throws Throwable {
        recordGetScriptTagWriterMapFromJspContext(getScriptTagWriterMap());

        getHelper().replay();

        Assert.assertSame("map is wrong instance", getScriptTagWriterMap(), PrivateAccessor
                .invoke(getObjectUnderTest(), "getScriptTagWriterMapFromJspContext",
                        null, null));
    }

    /**
     * Test findScriptTagWriterFactory.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testGetScriptTagwriterFactoryWhenTagAttributeNotNull()
            throws Throwable {
        getObjectUnderTest().setScriptTagWriterFactory(getMockScriptTagWriterFactory());

        getHelper().replay();

        Assert.assertSame("ScriptTagWriterFactory is wrong instance",
                getMockScriptTagWriterFactory(), PrivateAccessor.invoke(
                        getObjectUnderTest(), "findScriptTagWriterFactory",
                        null, null));

    }

    /**
     * Test findScriptTagWriterFactory.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testGetScriptTagwriterFactoryWhenFindAttributeNotNull()
            throws Throwable {
        getObjectUnderTest().setScriptTagWriterFactory(null);

        EasyMock.expect(
                getMockJspContext().findAttribute(
                        ScriptTag.SCRIPT_WRITER_FACTORY_ATTRIBUTE_NAME))
                .andReturn(getMockScriptTagWriterFactory()).atLeastOnce();

        getHelper().replay();

        Assert.assertSame("ScriptTagWriterFactory is wrong instance",
                getMockScriptTagWriterFactory(), PrivateAccessor.invoke(
                        getObjectUnderTest(), "findScriptTagWriterFactory",
                        null, null));
    }

    /**
     * Test findScriptTagWriterFactory.
     *
     * @throws Throwable
     *             Thrown if any error occurs.
     */
    @Test
    public void testGetScriptTagWriterFactoryWhenTagAttributeAndFindAttributeAreNull()
            throws Throwable {
        getObjectUnderTest().setScriptTagWriterFactory(null);

        EasyMock.expect(
                getMockJspContext().findAttribute(
                        ScriptTag.SCRIPT_WRITER_FACTORY_ATTRIBUTE_NAME))
                .andReturn(null).atLeastOnce();

        getHelper().replay();

        final Object actualScriptTagWriterFactory = PrivateAccessor.invoke(getObjectUnderTest(),
                "findScriptTagWriterFactory", null, null);
        Assert.assertTrue("ScriptTagWriterFactory is wrong type",
                McsScriptTagWriterFactory.class.isInstance(actualScriptTagWriterFactory));
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    private void setObjectUnderTest(final ScriptTag objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

    /**
     * @return the objectUnderTest
     */
    protected ScriptTag getObjectUnderTest() {
        return objectUnderTest;
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
     * @return the mockScriptTagWriter
     */
    public ScriptTagWriter getMockScriptTagWriter() {
        return mockScriptTagWriter;
    }

    /**
     * @param mockScriptTagWriter the mockScriptTagWriter to set
     */
    public void setMockScriptTagWriter(final ScriptTagWriter scriptTagWriter) {
        mockScriptTagWriter = scriptTagWriter;
    }

    /**
     * @return the mockScriptTagWriterFactory
     */
    public ScriptTagWriterFactory getMockScriptTagWriterFactory() {
        return mockScriptTagWriterFactory;
    }

    /**
     * @param mockScriptTagWriterFactory the mockScriptTagWriterFactory to set
     */
    public void setMockScriptTagWriterFactory(
            final ScriptTagWriterFactory mockScriptTagWriterFactory) {
        this.mockScriptTagWriterFactory = mockScriptTagWriterFactory;
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

    /**
     * @param scriptTagWriterMap the scriptTagWriterMap to set
     */
    private void setScriptTagWriterMap(final Map<String, ScriptTagWriter> scriptTagWriterMap) {
        this.scriptTagWriterMap = scriptTagWriterMap;
    }

    /**
     * @return the scriptTagWriterMap
     */
    private Map<String, ScriptTagWriter> getScriptTagWriterMap() {
        return scriptTagWriterMap;
    }

    private void recordGetScriptTagWriterMapFromJspContext(
            final Map<String, ScriptTagWriter> scriptTagWriterMapToReturn) {
        EasyMock.expect(
                getMockJspContext().getAttribute(
                        ScriptTag.SCRIPT_WRITER_MAP_ATTRIBUTE_NAME,
                        PageContext.REQUEST_SCOPE)).andReturn(scriptTagWriterMapToReturn)
                .atLeastOnce();
    }

    private void recordCreateScriptTagWriter() {
        EasyMock.expect(
                getMockScriptTagWriterFactory().createScriptTagWriter(DEFAULT_SRC,
                        DEFAULT_NAME, DEFAULT_TYPE)).andReturn(
                getMockScriptTagWriter());
    }

    /**
     * @return the scriptTagExtraInfoUnderTest
     */
    public ScriptTagExtraInfo getScriptTagExtraInfoUnderTest() {
        return scriptTagExtraInfoUnderTest;
    }

    /**
     * @param scriptTagExtraInfoUnderTest the scriptTagExtraInfoUnderTest to set
     */
    public void setScriptTagExtraInfoUnderTest(
            final ScriptTagExtraInfo scriptTagExtraInfoUnderTest) {
        this.scriptTagExtraInfoUnderTest = scriptTagExtraInfoUnderTest;
    }

    /**
     * @return the mockTagData
     */
    public TagData getMockTagData() {
        return mockTagData;
    }

    /**
     * @param mockTagData the mockTagData to set
     */
    public void setMockTagData(final TagData mockTagData) {
        this.mockTagData = mockTagData;
    }

}
