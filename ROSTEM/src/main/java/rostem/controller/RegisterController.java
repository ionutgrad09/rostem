package rostem.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static rostem.utils.ApiResponses.REQUEST_OK;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.model.users.RostemUser;
import rostem.service.authentication.RegisterService;
import rostem.utils.exception.RostemException;

@RestController
@Api(value = "register", description = "All operations that are used for creating accounts")
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @ApiOperation(value = "Register a new user",
            notes = "errors: User already defined, Email service no available, Invalid email format, Account registered but not activated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered user"),
            @ApiResponse(code = 400, message = "Some error occurred, check error field for more details")
    })
    @PostMapping(path = "/register", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> register(@RequestBody @Validated RostemUser user) {

        try {
            registerService.registerUser(user);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Activate an user",
            response = Response.class,
            notes = "errors: Invalid account key")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully activated user"),
            @ApiResponse(code = 400, message = "Some error occurred, check error field for more details")
    })
    @GetMapping(path = "/activate/{id}")
    public ResponseEntity<Response> activateAcc(@PathVariable(name = "id") String id) {
        String error = registerService.activateUser(id);
        if (error.equals(REQUEST_OK)) {
            return ResponseBuilder.encode(HttpStatus.OK);
        }
        return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, error);
    }

    @ApiOperation(value = "Activate an invited user/Accept invitation",
            notes = "errors: Invalid account key")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully accepted user"),
            @ApiResponse(code = 400, message = "Some error occurred, check error field for more details")
    })
    @PostMapping(path = "/acceptInvite/{id}")
    public ResponseEntity<Response> acceptInvite(@PathVariable(name = "id") String id, String password) {
        String error = registerService.acceptUser(id, password);
        if (error.equals(REQUEST_OK)) {
            return ResponseBuilder.encode(HttpStatus.OK);
        }
        return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, error);
    }

    @ApiOperation(value = "Invite an assistant",
            notes = "errors: User already defined, Email service not available, Account registered but not activated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully invited assistant"),
            @ApiResponse(code = 400, message = "Some error occurred, check error field for more details")
    })
    @PostMapping(path = "/inviteFriend")
    //TODO: limit the professor to be the only one able to access this api
    public ResponseEntity<Response> inviteAssistant(String email, String name) {
        String error = registerService.inviteFriend(email, name);
        if (error.equals(REQUEST_OK)) {
            return ResponseBuilder.encode(HttpStatus.OK);
        }
        return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, error);
    }
}
