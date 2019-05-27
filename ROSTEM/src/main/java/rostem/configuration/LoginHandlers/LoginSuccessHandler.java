package rostem.configuration.LoginHandlers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rostem.model.users.UserLoginDetail;
import rostem.model.users.UserPrincipal;

/**
 * Used to handle a successful authentification
**/
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    //
    // This method is called whenever an authentification is successful; it redirects the caller to "login/returndetails" (LoginController class)
    // in order to return the user details and caches the request for further usage
    //
    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws ServletException, IOException {
        logger.debug("[INFO] A login request was successful");
        final SavedRequest savedRequest = requestCache.getRequest(request, response);

        UserLoginDetail userdata = ((UserPrincipal)authentication.getPrincipal()).getUser();
        request.setAttribute("user", userdata);
        RequestDispatcher dispatcher = request.getRequestDispatcher("login/returndetails");
        dispatcher.forward(request,response);

        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            return;
        }
        final String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return;
        }
    }
}