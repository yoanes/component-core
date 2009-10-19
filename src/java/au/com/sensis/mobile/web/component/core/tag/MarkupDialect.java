package au.com.sensis.mobile.web.component.core.tag;

/**
 * Represents a JSP markup dialect, such as Volantis.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public interface MarkupDialect {

    /**
     * Enumeration containing all possible ids of {@link MarkupDialect} implementations.
     */
    enum MarkupDialectId {
        /***
         * Id of the Volantis dialect.
         */
        VOLANTIS
    }

    /**
     * @return unique identifier of this dialect.
     */
    MarkupDialectId getId();

    /**
     * {@link ScriptTagWriterFactory} that creates a {@link ScriptTagWriter} for
     * this dialect.
     *
     * @return {@link ScriptTagWriterFactory} that creates a
     *         {@link ScriptTagWriter} for this dialect.
     */
    ScriptTagWriterFactory getScriptTagWriterFactory();
}
