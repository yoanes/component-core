package au.com.sensis.mobile.web.component.core.sdpcommon.web;

import javax.servlet.http.HttpServletRequest;

/**
 * Supports analysis / extraction of the headers in the request to infer information about the
 * "visitor" to the web site.
 *
 * @author Boyd Sharrock (cloned from TPM)
 */
public interface HeaderInterpreter {

    /**
     * Analyse / Extract the headers and produce a Visitor that represents the inferred information.
     *
     * @param request   the {@link HttpServletRequest}.
     *
     * @return  a {@link Visitor}.
     */
    Visitor interpretHeaders(HttpServletRequest request);
}
