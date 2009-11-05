package au.com.sensis.mobile.web.component.core.sdpcommon.web.struts2;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * Useful convenience / common behaviour for all interceptors.
 *
 * @author Boyd Sharrock
 */
@SuppressWarnings("serial")
public abstract class AbstractInterceptor
        extends com.opensymphony.xwork2.interceptor.AbstractInterceptor {

    /**
     * Gets the underlying HttpServletRequest for this invocation.
     *
     * @param invocation    the {@link ActionInvocation} from the interceptor.
     *
     * @return  a {@link HttpServletRequest}
     */
    public HttpServletRequest getRequest(final ActionInvocation invocation) {

        return (HttpServletRequest)
                invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
    }
}
