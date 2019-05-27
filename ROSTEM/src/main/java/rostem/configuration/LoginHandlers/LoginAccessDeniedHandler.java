package rostem.configuration.LoginHandlers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Used to handle invalid login requests
 **/
@Component
public class LoginAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginAccessDeniedHandler.class);

    //
    // This method is called whenever access is denied; it redirects the caller to "login/returnerror" (LoginController class)
    //
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException ex) throws IOException, ServletException {
        logger.warn("[WARNING] A login request denied with exception: "+ex.getMessage());
        response.setStatus(403);
        request.setAttribute("error",ex);
        RequestDispatcher dispatcher = request.getRequestDispatcher("login/returnerror");
        dispatcher.forward(request,response);
    }

}