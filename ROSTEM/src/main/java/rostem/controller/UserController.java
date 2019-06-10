package rostem.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.request.RequestRostemUser;
import rostem.service.users.RostemUserService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api(description = "All operations that are used for editing a user")
@RequestMapping("/users")
public class UserController {

    private final RostemUserService rostemUserService;

    @Autowired
    public UserController(RostemUserService rostemUserService) {
        this.rostemUserService = rostemUserService;
    }

    @ApiOperation(value = "Get a user details by email.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The details were returned."),
            @ApiResponse(code = 400, message = "The user does not exists.")
    })
    @GetMapping(path = "/details/{email}")
    public ResponseEntity<Response> getUserDetails(@PathVariable("email") String email) {
        try {
            return ResponseBuilder.encode(HttpStatus.OK, rostemUserService.getUserByEmail(email));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Update user's info.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 400, message = "Some error occurred, check error field for more details")
    })
    @PutMapping(path = "/update")
    public ResponseEntity<Response> updateUser(@RequestBody RequestRostemUser requestRostemUser) {
        try {
            rostemUserService.updateUser(requestRostemUser);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
