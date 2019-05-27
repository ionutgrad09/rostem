package rostem.configuration.LoginHandlers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * Used to handle unsuccessful authentification
 **/
@Component
public class LoginFailureHanlder extends SimpleUrlAuthenticationFailureHandler {

    //
    // This method is called whenever an authentification is unsuccessful; it redirects the caller to "login/returnerror" (LoginController class)
    //
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.warn("[WARNING] A login request failed with exception: "+exception.getMessage());
        request.setAttribute("error", exception);
        RequestDispatcher dispatcher = request.getRequestDispatcher("login/returnerror");
        dispatcher.forward(request,response);
        return;
    }
}
