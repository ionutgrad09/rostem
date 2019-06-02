package rostem.controller.material;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.request.RequestActionChapter;
import rostem.model.dto.request.RequestRecentPosts;
import rostem.model.dto.response.ResponseChapter;
import rostem.service.material.ChapterService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api("Controller used for managing the chapters.")
@RequestMapping("/chapters")
public class ChapterController {

    private static final String ACTION_TODO = "TODO";
    private static final String ACTION_DONE = "DONE";

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
    @PostMapping(path = "/action")
    public ResponseEntity<Response> getAllChaptersForTutorial(@RequestBody @Validated RequestActionChapter requestActionChapter) {
        try {
            List<ResponseChapter> chapters = chapterService.getChaptersForTutorial(requestActionChapter);
            return ResponseBuilder.encode(HttpStatus.OK, chapters, 0, chapters.size(), chapters.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all chapters",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The chapters were returned."),
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

    @ApiOperation(value = "Get latest chapters",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The latest chapters were returned."),
    })
    @PostMapping("/latest")
    public ResponseEntity<Response> getLatestChapters(@RequestBody RequestRecentPosts requestRecentPosts) {
        try {
            List<ResponseChapter> chapters = chapterService.getLatestChapters(requestRecentPosts);
            return ResponseBuilder.encode(HttpStatus.OK, chapters, 0, chapters.size(), chapters.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Mark chapter as done/todo.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The chapter was marked."),
    })
    @PostMapping("/mark")
    public ResponseEntity<Response> markChapter(@RequestBody RequestActionChapter requestActionChapter) {
        try {
            if (requestActionChapter.getActionType().equals(ACTION_DONE)) {
                chapterService.markChapterAsDone(requestActionChapter.getEmail(), requestActionChapter.getChapterId());
            } else if (requestActionChapter.getActionType().equals(ACTION_TODO)) {
                chapterService.markChapterAsTodo(requestActionChapter.getEmail(), requestActionChapter.getChapterId());
            } else {
                return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, "Action type is wrong.");
            }
            return ResponseBuilder.encode(HttpStatus.OK);

        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Unmark chapter as done/todo.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The chapter was marked."),
    })
    @PostMapping("/unmark")
    public ResponseEntity<Response> unmarkChapter(@RequestBody RequestActionChapter requestActionChapter) {
        try {
            if (requestActionChapter.getActionType().equals(ACTION_DONE)) {
                chapterService.unmarkDoneChapter(requestActionChapter.getEmail(), requestActionChapter.getChapterId());
            } else if (requestActionChapter.getActionType().equals(ACTION_TODO)) {
                chapterService.unmarkTodoChapter(requestActionChapter.getEmail(), requestActionChapter.getChapterId());
            } else {
                return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, "Action type is wrong.");
            }
            return ResponseBuilder.encode(HttpStatus.OK);

        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
