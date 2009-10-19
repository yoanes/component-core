package au.com.sensis.mobile.web.component.core.tag;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.sensis.wireless.test.AbstractJUnit4TestCase;

/**
 * Unit test {@link McsScriptTagWriter}.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class McsScriptTagWriterFactoryTestCase extends AbstractJUnit4TestCase {

    private static final String DEFAULT_SRC = "/comp/map/scripts/map-component.mscr";
    private static final String DEFAULT_NAME = "deviceInfo";
    private static final String DEFAULT_TYPE = "text/javascript";

    private McsScriptTagWriterFactory objectUnderTest;

    /**
     * Setup test data.
     *
     * @throws Exception Thrown if any error occurs.
     */
    @Before
    public void setUp() throws Exception {
        setObjectUnderTest(new McsScriptTagWriterFactory());
    }

    @Test
    public void testCreateScriptTagWriter() throws Throwable {
        final ScriptTagWriter actualScriptTagWriter =
                getObjectUnderTest().createScriptTagWriter(DEFAULT_SRC,
                        DEFAULT_NAME, DEFAULT_TYPE);
        Assert.assertNotNull("actual ScriptTagWriter should not be null",
                actualScriptTagWriter);
        Assert.assertTrue(
                "actual ScriptTagWriter should be an instance of McsScriptTagWriter",
                McsScriptTagWriter.class
                        .isInstance(actualScriptTagWriter));
    }

    /**
     * @return the objectUnderTest
     */
    public McsScriptTagWriterFactory getObjectUnderTest() {
        return objectUnderTest;
    }

    /**
     * @param objectUnderTest the objectUnderTest to set
     */
    public void setObjectUnderTest(final McsScriptTagWriterFactory objectUnderTest) {
        this.objectUnderTest = objectUnderTest;
    }

}
