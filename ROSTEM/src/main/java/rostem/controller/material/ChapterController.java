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
import rostem.model.dto.response.ResponseChapter;
import rostem.service.material.ChapterService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api("Controller used for managing the chapters.")
@RequestMapping("/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @ApiOperation(value = "Get all chapters for a tutorial",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The chapters were returned."),
            @ApiResponse(code = 400, message = "The tutorial does not exist.")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Response> getAllChaptersForTutorial(@PathVariable("id") Long id) {
        try {
            List<ResponseChapter> chapters = chapterService.getChaptersForTutorial(id);
            return ResponseBuilder.encode(HttpStatus.OK, chapters, 0, chapters.size(), chapters.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all chapters",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The chapters were returned."),
            @ApiResponse(code = 400, message = "The tutorial does not exist.")
    })
    @GetMapping
    public ResponseEntity<Response> getAllChapters() {
        try {
            List<ResponseChapter> chapters = chapterService.getAllChapters();
            return ResponseBuilder.encode(HttpStatus.OK, chapters, 0, chapters.size(), chapters.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
