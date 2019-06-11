package rostem.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.UserLoginDetailDto;
import rostem.model.users.UserLoginDetail;
import rostem.model.users.UserPrincipal;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import springfox.documentation.annotations.ApiIgnore;

@Api("Used to sent user details or errors at login")
@RestController
public class LoginController {

    @ApiOperation("Returns user details")
    @PostMapping(value = "login/returndetails")
    public ResponseEntity<Response> returnLoggedUserDetails(HttpServletRequest request) {
        UserLoginDetail logged_user = (UserLoginDetail) request.getAttribute("user");
        return ResponseBuilder.encode(HttpStatus.OK, new UserLoginDetailDto(logged_user));
    }

    @ApiOperation("Returns login error")
    @PostMapping(value = "login/returnerror")
    public ResponseEntity<Response> ReturnError(HttpServletRequest request) {
        RuntimeException exception = (RuntimeException) request.getAttribute("error");
        return ResponseBuilder.encode(HttpStatus.OK, exception.getMessage());
    }

    @PostMapping(value = "login/getdetails")
    public ResponseEntity<Response> getUserDetails(@ApiIgnore Principal principal) {
        if (principal == null) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, "invalidUser");
        }
        UserPrincipal userPrincipal = (UserPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseBuilder.encode(HttpStatus.OK, new UserLoginDetailDto(userPrincipal.getUser()));
    }
}
