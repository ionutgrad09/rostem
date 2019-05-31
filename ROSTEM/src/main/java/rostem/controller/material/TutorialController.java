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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.response.ResponseTutorial;
import rostem.service.material.TutorialService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api("Controller used for managing the tutorials.")
@RequestMapping("/tutorials")
public class TutorialController {

    private final TutorialService tutorialService;

    @Autowired
    public TutorialController(TutorialService tutorialService) {
        this.tutorialService = tutorialService;
    }

    @ApiOperation(value = "Get all tutorials for a category",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The tutorials were returned."),
            @ApiResponse(code = 400, message = "The category does not exist.")
    })
    @GetMapping(path = "/{categoryName}")
    public ResponseEntity<Response> getAllTutorialsForCategory(@PathVariable("categoryName") String categoryName) {
        try {
            List<ResponseTutorial> tutorials = tutorialService.getTutorialsForCategory(categoryName);
            return ResponseBuilder.encode(HttpStatus.OK, tutorials, 0, tutorials.size(), tutorials.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all tutorials",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The tutorials were returned."),
            @ApiResponse(code = 400, message = "The category does not exist.")
    })
    @GetMapping
    public ResponseEntity<Response> getAllTutorials() {
        try {
            List<ResponseTutorial> tutorials = tutorialService.getAllTutorials();
            return ResponseBuilder.encode(HttpStatus.OK, tutorials, 0, tutorials.size(), tutorials.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
