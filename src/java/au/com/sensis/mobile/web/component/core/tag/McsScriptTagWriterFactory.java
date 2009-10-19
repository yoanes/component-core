package au.com.sensis.mobile.web.component.core.tag;

/**
 * {@link ScriptTagWriterFactory} implementation that creates {@link McsScriptTagWriter} instances.
 */
public class McsScriptTagWriterFactory implements
        ScriptTagWriterFactory {

    /**
     * {@inheritDoc}
     */
    public McsScriptTagWriter createScriptTagWriter(final String src,
            final String name, final String type) {
        return new McsScriptTagWriter(src, name, type);
    }
}
