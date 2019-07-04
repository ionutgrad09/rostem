package rostem.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.request.BatchDeleteCategories;
import rostem.model.dto.request.BatchDeleteChapters;
import rostem.model.dto.request.BatchDeleteTutorials;
import rostem.model.dto.request.BatchDeleteUsers;
import rostem.model.dto.request.RequestCategory;
import rostem.model.dto.request.RequestChapter;
import rostem.model.dto.request.RequestRostemUser;
import rostem.model.dto.request.RequestTutorial;
import rostem.model.dto.response.ResponseChapter;
import rostem.model.dto.response.ResponseRostemUser;
import rostem.model.dto.response.ResponseStatisticsCategory;
import rostem.model.dto.response.ResponseStatisticsChapter;
import rostem.model.dto.response.ResponseStatisticsUser;
import rostem.service.material.CategoryService;
import rostem.service.material.ChapterService;
import rostem.service.material.StatisticsService;
import rostem.service.material.TutorialService;
import rostem.service.users.RostemUserService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api("Controller used by the admin")
@RequestMapping("/admin")
public class AdminController {

    private final RostemUserService rostemUserService;
    private final CategoryService categoryService;
    private final ChapterService chapterService;
    private final TutorialService tutorialService;
    private final StatisticsService statisticsService;

    @Autowired
    public AdminController(RostemUserService rostemUserService, CategoryService categoryService,
            TutorialService tutorialService,
            ChapterService chapterService, StatisticsService statisticsService) {
        this.rostemUserService = rostemUserService;
        this.categoryService = categoryService;
        this.tutorialService = tutorialService;
        this.chapterService = chapterService;
        this.statisticsService = statisticsService;
    }

    @ApiOperation(value = "Get all users",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The users were returned."),
    })
    @GetMapping(path = "/users")
    public ResponseEntity<Response> getAllUsers() {
        try {
            List<ResponseRostemUser> rostemUsers = rostemUserService.getAllUsers();
            return ResponseBuilder.encode(HttpStatus.OK, rostemUsers, 0, rostemUsers.size(), rostemUsers.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete user by email",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was deleted."),
            @ApiResponse(code = 400, message = "The user does not exist.")
    })
    @DeleteMapping(path = "/users")
    public ResponseEntity<Response> deleteUsers(@RequestBody BatchDeleteUsers batchDeleteUsers) {
        try {
            rostemUserService.deleteUserByEmail(batchDeleteUsers.getEmails());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Update user by email",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user was updated."),
            @ApiResponse(code = 400, message = "The user does not exist.")
    })
    @PutMapping(path = "/users")
    public ResponseEntity<Response> updateUser(@RequestBody RequestRostemUser requestRostemUser) {
        try {
            rostemUserService.updateUser(requestRostemUser);
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @ApiOperation(value = "Create a new category",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The category was created."),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 409, message = "The category already exists.")
    })
    @PostMapping("/createCategory")
    public ResponseEntity<Response> createCategory(@RequestBody @Validated RequestCategory requestCategory) {
        try {
            return ResponseBuilder.encode(HttpStatus.CREATED, categoryService.createCategory(requestCategory));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete a category",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was deleted."),
            @ApiResponse(code = 400, message = "The category does not exist.")
    })
    @DeleteMapping("/deleteCategories")
    public ResponseEntity<Response> deleteCategories(@RequestBody BatchDeleteCategories batchDeleteCategories) {
        try {
            categoryService.deleteCategories(batchDeleteCategories.getIds());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Update a category",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was updated."),
            @ApiResponse(code = 400, message = "The category does not exist.")
    })
    @PutMapping(path = "/updateCategory/{id}")
    public ResponseEntity<Response> updateCategory(@PathVariable("id") Long id,
            @RequestBody RequestCategory requestCategory) {
        try {
            return ResponseBuilder.encode(HttpStatus.OK, categoryService.updateCategory(id, requestCategory));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Create a new chapter",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The chapter was created."),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 409, message = "The chapter already exists.")
    })
    @PostMapping("/createChapter")
    public ResponseEntity<Response> createChapter(@RequestBody @Validated RequestChapter requestChapter) {
        try {
            return ResponseBuilder.encode(HttpStatus.CREATED, chapterService.createChapter(requestChapter));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @ApiOperation(value = "Deletes a list of chapters",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The chapters were deleted."),
            @ApiResponse(code = 400, message = "Error when trying to delete the chapters.")
    })
    @DeleteMapping("/deleteChapters")
    public ResponseEntity<Response> deleteChapters(@RequestBody BatchDeleteChapters batchDeleteChapters) {
        try {
            chapterService.deleteChapters(batchDeleteChapters.getIds());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Update a chapter",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The chapter was updated."),
            @ApiResponse(code = 400, message = "The chapter does not exist.")
    })
    @PutMapping(path = "/updateChapter/{id}")
    public ResponseEntity<Response> updateChapter(@PathVariable("id") Long id,
            @RequestBody @Validated RequestChapter requestChapter) {
        try {
            return ResponseBuilder.encode(HttpStatus.OK, chapterService.updateChapter(id, requestChapter));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Create a new tutorial",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The tutorial was created."),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 409, message = "The tutorial already exists.")
    })
    @PostMapping("/createTutorial")
    public ResponseEntity<Response> createTutorial(@RequestBody @Validated RequestTutorial requestTutorial) {
        try {
            return ResponseBuilder.encode(HttpStatus.CREATED, tutorialService.createTutorial(requestTutorial));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @ApiOperation(value = "Deletes a list of tutorials",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The tutorials were deleted."),
            @ApiResponse(code = 400, message = "Error when trying to delete the tutorials.")
    })
    @DeleteMapping("/deleteTutorials")
    public ResponseEntity<Response> deleteTutorials(@RequestBody BatchDeleteTutorials batchDeleteTutorials) {
        try {
            tutorialService.deleteTutorials(batchDeleteTutorials.getIds());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Update a tutorial",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The tutorial was updated."),
            @ApiResponse(code = 400, message = "The tutorial does not exist.")
    })
    @PutMapping(path = "/updateTutorial/{id}")
    public ResponseEntity<Response> updateTutorial(@PathVariable("id") Long id,
            @RequestBody @Validated RequestTutorial requestTutorial) {
        try {
            return ResponseBuilder.encode(HttpStatus.OK, tutorialService.updateTutorial(id, requestTutorial));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    /*********** STATISTICS ***************/

    @ApiOperation(value = "Get top chapters.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The  chapters were returned."),
    })
    @GetMapping(path = "/statistics/chapters")
    public ResponseEntity<Response> getTopChapters() {
        try {
            List<ResponseStatisticsChapter> chapters = statisticsService.getTopChapters();
            return ResponseBuilder.encode(HttpStatus.OK, chapters, 0, chapters.size(), chapters.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get top categories.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The categories were returned."),
    })
    @GetMapping(path = "/statistics/categories")
    public ResponseEntity<Response> getTopCategories() {
        try {
            List<ResponseStatisticsCategory> categories = statisticsService.getTopCategories();
            return ResponseBuilder.encode(HttpStatus.OK, categories, 0, categories.size(), categories.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get top users.",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The users were returned."),
    })
    @GetMapping(path = "/statistics/users")
    public ResponseEntity<Response> getTopUsers() {
        try {
            List<ResponseStatisticsUser> users = statisticsService.getTopUsers();
            return ResponseBuilder.encode(HttpStatus.OK, users, 0, users.size(), users.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
