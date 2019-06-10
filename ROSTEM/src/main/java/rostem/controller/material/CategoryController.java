package rostem.controller.material;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.request.RequestFavoriteCategory;
import rostem.model.dto.response.ResponseCategory;
import rostem.service.material.CategoryService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api("Controller used for managing the categories.")
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Get a category by id",
            response = Response.class,
            notes = "errors: Wrong category id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was found."),
            @ApiResponse(code = 404, message = "The category does not exist.")
    })
    @GetMapping(path = "/category/{id}")
    public ResponseEntity<Response> getCategory(@PathVariable("id") Long id) {
        try {
            return ResponseBuilder.encode(HttpStatus.OK, this.categoryService.getCategoryById(id));
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all categories",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The categories were returned."),
    })
    @GetMapping("/{email}")
    public ResponseEntity<Response> getCategories(@PathVariable("email") String userEmail) {
        try {
            List<ResponseCategory> categories = this.categoryService.getAllCategories(userEmail);
            return ResponseBuilder.encode(HttpStatus.OK, categories, 0, categories.size(), categories.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all categories",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The categories were returned."),
    })
    @GetMapping
    public ResponseEntity<Response> getCategoriesForAdmin() {
        try {
            List<ResponseCategory> categories = this.categoryService.getAllCategoriesForAdmin();
            return ResponseBuilder.encode(HttpStatus.OK, categories, 0, categories.size(), categories.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Get all favorite categories for a user",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The categories were returned."),
    })
    @GetMapping("/favorites/{email}")
    public ResponseEntity<Response> getFavoriteCategories(@PathVariable("email") String email) {
        try {
            List<ResponseCategory> categories = this.categoryService.getFavoritesCategories(email);
            return ResponseBuilder.encode(HttpStatus.OK, categories, 0, categories.size(), categories.size());
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Add a new category to the favorite list of a user",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was added."),
            @ApiResponse(code = 400, message = "Bad request.")
    })
    @PostMapping("/favorites")
    public ResponseEntity<Response> addNewFavoriteCategory(
            @RequestBody RequestFavoriteCategory requestFavoriteCategory) {
        try {
            this.categoryService
                    .addFavoriteCategory(requestFavoriteCategory.getEmail(), requestFavoriteCategory.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Add a new category to the favorite list of a user",
            response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category was added."),
            @ApiResponse(code = 400, message = "Bad request.")
    })
    @DeleteMapping("/favorites")
    public ResponseEntity<Response> deleteFavoriteCategory(
            @RequestBody RequestFavoriteCategory requestFavoriteCategory) {
        try {
            this.categoryService
                    .deleteFavoriteCategory(requestFavoriteCategory.getEmail(), requestFavoriteCategory.getId());
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
