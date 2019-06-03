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
    public UtilController(TutorialService tutorialService, CategoryService categoryService,
            ChapterService chapterService,
            RegisterService registerService) {
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
            return ResponseBuilder.encode(HttpStatus.OK);
        } catch (RostemException e) {
            return ResponseBuilder.encode(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void addCategories() {
        RequestCategory requestCategory = new RequestCategory();
        requestCategory.setName("Math");
        requestCategory.setDescription("All you need to know about math.");
        category1 = categoryService.createCategory(requestCategory).getId();

        requestCategory.setName("Web development");
        requestCategory.setDescription("Learn JS, React and Angular");
        category2 = categoryService.createCategory(requestCategory).getId();

        requestCategory.setName("Spring Framework");
        requestCategory.setDescription("Learn everything about Spring Framework.");
        categoryService.createCategory(requestCategory);

        requestCategory.setName(".NET Framework");
        requestCategory.setDescription(".NET Framework at its best.");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("Science & engineering");
        requestCategory.setDescription("All you need to know about science & engineering.");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("Databases");
        requestCategory.setDescription("All you need to know about databases");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("Computing");
        requestCategory.setDescription("All you need to know about computing.");
        categoryService.createCategory(requestCategory);

    }

    private void addTutorials() {
        RequestTutorial requestTutorial = new RequestTutorial();

        requestTutorial.setCategoryId(category1);
        requestTutorial.setName("Pre-algebra");
        requestTutorial.setDescription("All you need to know about Pre-algebra");
        tutorial1 = tutorialService.createTutorial(requestTutorial).getId();

        requestTutorial.setName("Trigonometry");
        requestTutorial.setDescription("All you need to know about Trigonometry");
        tutorial2 = tutorialService.createTutorial(requestTutorial).getId();

        requestTutorial.setName("Algebra1");
        requestTutorial.setDescription("All you need to know about Algebra1");
        tutorialService.createTutorial(requestTutorial).getId();

        requestTutorial.setName("Algebra2");
        requestTutorial.setDescription("All you need to know about Algebra2");
        tutorialService.createTutorial(requestTutorial);

        requestTutorial.setName("Geometry");
        requestTutorial.setDescription("All you need to know about Geometry");
        tutorialService.createTutorial(requestTutorial);

        requestTutorial.setName("Statistics & Probability");
        requestTutorial.setDescription("All you need to know about Statistics & Probability");
        tutorialService.createTutorial(requestTutorial);

        requestTutorial.setName("Differential equations");
        requestTutorial.setDescription("All you need to know about Differential equations");
        tutorialService.createTutorial(requestTutorial);
    }

    private void addChapters() {
        RequestChapter requestChapter = new RequestChapter();

        requestChapter.setTutorialId(tutorial1);
        requestChapter.setName("Arithmetic properties");
        requestChapter.setUrl("https://www.youtube.com/embed/edxiROADl8A");
        requestChapter.setDescription(
                "Multiplication and addition have specific arithmetic properties which characterize those operations. "
                        + "In no specific order, they are the commutative, associative, distributive, identity and inverse properties.");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("Factors and multiples");
        requestChapter.setDescription("Learn about factors and multiples and how they relate to each other.");
        requestChapter.setUrl("https://www.youtube.com/embed/rUrLuTMq-sw");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("Fractions");
        requestChapter.setDescription(
                "A fraction (from Latin fractus, \"broken\") represents a part of a whole or, more generally,"
                        + " any number of equal parts. When spoken in everyday English.");
        requestChapter.setUrl("https://www.youtube.com/embed/n0FZhQ_GkKw");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("Decimals");
        requestChapter.setDescription(
                "The decimal numeral system (also called base-ten positional numeral system, and occasionally"
                        + " called denary) is the standard system for denoting integer and non-integer numbers.");
        requestChapter.setUrl("https://www.youtube.com/embed/kwh4SD1ToFc");
        chapterService.createChapter(requestChapter);

        requestChapter.setTutorialId(tutorial2);

        requestChapter.setName("Trigonometry with right triangles");
        requestChapter.setDescription("Learn about trigonometry with right triangles.");
        requestChapter.setUrl("https://www.youtube.com/embed/l5VbdqRjTXc");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("Trigonometric equations and identities");
        requestChapter.setDescription("Trigonometric equations and identities.");
        requestChapter.setUrl("https://www.youtube.com/embed/VMAMARmmDac");
        chapterService.createChapter(requestChapter);


    }
}
