package au.com.sensis.mobile.web.component.core.tag;

/**
 * Factory for {@link ScriptTagWriter}s to insulate clients from the particular
 * target markup dialect of the script tag.
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public interface ScriptTagWriterFactory {
    /**
     * Create a {@link ScriptTagWriter} instance. Note that this factory
     * interface assumes that the value of {@link ScriptTagWriter#getId()} is
     * derived from other attributes.
     *
     * @param src
     *            See {@link ScriptTagWriter#getSrc()}.
     * @param name
     *            See {@link ScriptTagWriter#getName()}.
     * @param type
     *            See {@link ScriptTagWriter#getType()}.
     * @return a new {@link ScriptTagWriter}.
     */
    ScriptTagWriter createScriptTagWriter(final String src, final String name,
            final String type);
}
