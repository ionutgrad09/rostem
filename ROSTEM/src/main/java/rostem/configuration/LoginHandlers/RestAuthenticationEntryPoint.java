package rostem.configuration.LoginHandlers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * The Entry Point will not redirect to any sort of Login - it will return 401
 */
@Component
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) throws IOException, ServletException {

        request.setAttribute("error", authException);
        RequestDispatcher dispatcher = request.getRequestDispatcher("login/returnerror");
        dispatcher.forward(request, response);
    }

}