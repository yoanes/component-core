package au.com.sensis.mobile.web.component.logging.spring;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.Log4jNestedDiagnosticContextInterceptor;
import org.springframework.web.context.request.WebRequest;

import au.com.sensis.wireless.web.common.exception.ApplicationRuntimeException;
import au.com.sensis.wireless.web.common.validation.Validatable;
import au.com.sensis.wireless.web.common.validation.ValidatableUtils;

/**
 * Specialisation of {@link Log4jNestedDiagnosticContextInterceptor} that also
 * logs the request uri being handled before and after it is handled. Both of
 * these log entries are guaranteed to be stamped with the
 * {@link #getNestedDiagnosticContextMessage(WebRequest)}, courtesy of the
 * inherited {@link Log4jNestedDiagnosticContextInterceptor}.
 * <p>
 * This class also allows and requires the NDC message to be injected via
 * {@link #setNdcMessage(String)}.
 * </p>
 *
 * @author Adrian.Koh2@sensis.com.au
 */
public class EntryAndExitLoggingWebRequestInterceptor extends
        Log4jNestedDiagnosticContextInterceptor implements Validatable {

    private static final Logger LOGGER =
            Logger.getLogger(EntryAndExitLoggingWebRequestInterceptor.class);

    private String ndcMessage;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getNestedDiagnosticContextMessage(final WebRequest request) {
        return getNdcMessage();
    }

    /**
     * @return the ndcMessage
     */
    public String getNdcMessage() {
        return ndcMessage;
    }

    /**
     * @param ndcMessage
     *            the ndcMessage to set
     */
    public void setNdcMessage(final String ndcMessage) {
        this.ndcMessage = ndcMessage;
    }

    /**
     * {@inheritDoc}
     */
    public void validateState() throws ApplicationRuntimeException {
        ValidatableUtils
                .validateStringIsNotBlank(getNdcMessage(), "ndcMessage");
    }

    /**
     * In addition to the inherited method behaviour below, this implementation
     * also logs what request is being handled.
     *
     * {@inheritDoc}
     */
    @Override
    public void postHandle(final WebRequest request, final ModelMap model)
            throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exiting " + request.getDescription(false));
        }

        super.postHandle(request, model);
    }

    /**
     * In addition to the inherited method behaviour below, this implementation
     * also logs what request is being handled.
     *
     * {@inheritDoc}
     */
    @Override
    public void preHandle(final WebRequest request) throws Exception {
        super.preHandle(request);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entering " + request.getDescription(false));
        }

    }

}
