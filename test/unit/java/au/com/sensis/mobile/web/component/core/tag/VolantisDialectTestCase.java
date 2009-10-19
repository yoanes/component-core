package au.com.sensis.mobile.web.component.core.tag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.mobile.web.component.core.tag.MarkupDialect.MarkupDialectId;
import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link McsScriptTagWriter}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class VolantisDialectTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_SRC =
            "/comp/map/scripts/map-component.mscr";
    private static final String DEFAULT_NAME = "deviceInfo";
    private static final String DEFAULT_TYPE = "text/javascript";

    private VolantisDialect objectUnderTest;

    /**
     * Setup test data.
     *
     * @throws Exception
     *             Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new VolantisDialect());
    }

    @Test
    public void testGetId() throws Throwable {
        Assert.assertSame("id is wrong", MarkupDialectId.VOLANTIS,
                getObjectUnderTest().getId());
    }

    @Test
    public void testGetScriptTagWriterFactory() throws Throwable {
        final ScriptTagWriterFactory actualScriptTagWriterFactory =
            getObjectUnderTest().getScriptTagWriterFactory();
    Assert.assertNotNull("actual ScriptTagWriterFactory should not be null",
            actualScriptTagWriterFactory);
    Assert.assertTrue(
            "actual ScriptTagWriterFactory should be an instance of McsScriptTagWriterFactory",
            McsScriptTagWriterFactory.class
                    .isInstance(actualScriptTagWriterFactory));
    }

    @Test
    public void testGetInstance() throws Throwable {
        Assert.assertNotNull("getInstance() should not be null",
                VolantisDialect.getInstance());
    }

    /**
     * @return the objectUnderTest
     */
    public VolantisDialect getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest
     *            the objectUnderTest to set
     */
    public void setObjectUnderTest(final VolantisDialect objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

}
