package rostem.configuration;

import static rostem.utils.ApiResponses.HTTP_REQUEST_INVALID_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.filter.OncePerRequestFilter;


public class BadRequestFilter extends OncePerRequestFilter implements Ordered {

    private final ObjectMapper objectMapper;

    public BadRequestFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RequestRejectedException e) {
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put("message", HTTP_REQUEST_INVALID_VALUE);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(objectMapper.writeValueAsString(jsonMap));
        }
    }

    @Override
    public int getOrder() {
        return -2147483646;
    }
}
