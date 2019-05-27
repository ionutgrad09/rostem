package rostem.controller.material;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rostem.model.dto.request.RequestCategory;
import rostem.model.dto.request.RequestChapter;
import rostem.model.dto.request.RequestTutorial;
import rostem.model.users.User;
import rostem.service.authentication.RegisterService;
import rostem.service.material.CategoryService;
import rostem.service.material.ChapterService;
import rostem.service.material.TutorialService;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.ResponseBuilder.ResponseBuilder;
import rostem.utils.exception.RostemException;

@RestController
@Api("Controller used for managing the tutorials.")
@RequestMapping("/populate")
public class UtilController {

    private final TutorialService tutorialService;
    private final ChapterService chapterService;
    private final CategoryService categoryService;
    private final RegisterService registerService;

    private long category1;
    private long category2;

    private long tutorial1;
    private long tutorial2;

    @Autowired
    public UtilController(TutorialService tutorialService, CategoryService categoryService, ChapterService chapterService,
            RegisterService registerService){
        this.tutorialService = tutorialService;
        this.chapterService = chapterService;
        this.categoryService = categoryService;
        this.registerService = registerService;
    }

    @PostMapping()
    public ResponseEntity<Response> populateWithData() {
        try {
            addCategories();
            addTutorials();
            addChapters();
            addUsers();
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void addUsers(){
        User userA = new User("a@a.a","1234","a");
        User userB = new User("b@b.b","1234","b");
        User userC = new User("c@c.c","1234","c");

        registerService.registerUser(userA);
        registerService.registerUser(userB);
        registerService.registerUser(userC);
    }

    private void addCategories(){
        RequestCategory requestCategory = new RequestCategory();
        requestCategory.setName("category1");
        requestCategory.setDescription("description1");
        category1 = categoryService.createCategory(requestCategory).getId();

        requestCategory.setName("category2");
        requestCategory.setDescription("description2");
        category2 = categoryService.createCategory(requestCategory).getId();

        requestCategory.setName("category3");
        requestCategory.setDescription("description3");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("category4");
        requestCategory.setDescription("description4");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("category5");
        requestCategory.setDescription("description5");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("category6");
        requestCategory.setDescription("description6");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("category7");
        requestCategory.setDescription("description7");
        categoryService.createCategory(requestCategory);

    }

    private void addTutorials(){
        RequestTutorial requestTutorial = new RequestTutorial();

        requestTutorial.setCategoryId(category1);
        requestTutorial.setName("tutorial1");
        requestTutorial.setDescription("description1");

        tutorial1 = tutorialService.createTutorial(requestTutorial).getId();

        requestTutorial.setName("tutorial2");
        requestTutorial.setDescription("description2");

        tutorial2 = tutorialService.createTutorial(requestTutorial).getId();

        requestTutorial.setCategoryId(category2);
        requestTutorial.setName("tutorial3");
        requestTutorial.setDescription("description3");

        tutorialService.createTutorial(requestTutorial);

        requestTutorial.setCategoryId(category2);
        requestTutorial.setName("tutorial4");
        requestTutorial.setDescription("description4");

        tutorialService.createTutorial(requestTutorial);

        requestTutorial.setCategoryId(category2);
        requestTutorial.setName("tutorial5");
        requestTutorial.setDescription("description5");

        tutorialService.createTutorial(requestTutorial);
    }

    private void addChapters(){
        RequestChapter requestChapter = new RequestChapter();

        requestChapter.setTutorialId(tutorial1);
        requestChapter.setName("chapter1");
        requestChapter.setDescription("description1");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("chapter2");
        requestChapter.setDescription("description2");
        chapterService.createChapter(requestChapter);

        requestChapter.setTutorialId(tutorial2);
        requestChapter.setName("chapter3");
        requestChapter.setDescription("description3");
        chapterService.createChapter(requestChapter);
    }
}
