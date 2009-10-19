package au.com.sensis.mobile.web.component.core.tag;

/**
 * {@link MarkupDialect} targeted at producing markup for the Volantis MCS platform.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class VolantisDialect implements MarkupDialect {

    private static final VolantisDialect SINGLETON = new VolantisDialect();

    /**
     * {@inheritDoc}
     */
    public MarkupDialectId getId() {
        return MarkupDialectId.VOLANTIS;
    }

    /**
     * {@inheritDoc}
     */
    public ScriptTagWriterFactory getScriptTagWriterFactory() {
        return new McsScriptTagWriterFactory();
    }

    /**
     * @return {@link VolantisDialect} singleton instance.
     */
    public static final VolantisDialect getInstance() {
        return SINGLETON;
    }

}
