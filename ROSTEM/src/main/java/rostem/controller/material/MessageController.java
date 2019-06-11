package rostem.controller.material;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.request.RequestMessage;
import rostem.model.dto.response.ResponseMessage;
import rostem.service.material.MessageService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api("Controller used for managing the messages.")
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation(value = "Get all messages sent by a user.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The messages were returned."),
            @ApiResponse(code = 400, message = "The user does not exist.")
    })
    @GetMapping(path = "/sent/{email}")
    public ResponseEntity<Response> getAllSentMessages(@PathVariable("email") String email) {
        try {
            List<ResponseMessage> messages = messageService.getMessagesSent(email);
            return ResponseBuilder.encode(HttpStatus.OK, messages, 0, messages.size(), messages.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all messages received by a user.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The messages were returned."),
            @ApiResponse(code = 400, message = "The user does not exist.")
    })
    @GetMapping(path = "/received/{email}")
    public ResponseEntity<Response> getAllReceivedMessages(@PathVariable("email") String email) {
        try {
            List<ResponseMessage> messages = messageService.getMessagesReceived(email);
            return ResponseBuilder.encode(HttpStatus.OK, messages, 0, messages.size(), messages.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Send a new message.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The message was sent."),
            @ApiResponse(code = 400, message = "The user does not exist.")
    })
    @PostMapping(path = "/send")
    public ResponseEntity<Response> sendMessage(@RequestBody RequestMessage requestMessage) {
        try {
            messageService.addMessage(requestMessage);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
