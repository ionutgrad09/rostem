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
import rostem.model.users.RostemAdmin;
import rostem.model.users.RostemUser;
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
    private long tutorial1;
    private long tutorial2;
    private long tutorial3;

    @Autowired
    public UtilController(TutorialService tutorialService, CategoryService categoryService,
            ChapterService chapterService, RegisterService registerService) {
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

    private void addCategories() {
        RequestCategory requestCategory = new RequestCategory();
        requestCategory.setName("Math");
        requestCategory.setDescription("All you need to know about math.");
        category1 = categoryService.createCategory(requestCategory).getId();

        requestCategory.setName("Web development");
        requestCategory.setDescription("Learn JS, React and Angular");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("Spring Framework");
        requestCategory.setDescription("Learn everything about Spring Framework.");
        categoryService.createCategory(requestCategory);

        requestCategory.setName(".NET Framework");
        requestCategory.setDescription(".NET Framework at its best.");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("Science");
        requestCategory.setDescription("All you need to know about science.");
        categoryService.createCategory(requestCategory);

        requestCategory.setName("Engineering");
        requestCategory.setDescription("All you need to know about engineering.");
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
        tutorial3 = tutorialService.createTutorial(requestTutorial).getId();

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
        requestChapter.setName("Ratios");
        requestChapter.setContent(
                "<p><span style=\"color: rgb(226,80,65);\"><strong><ins>Simple Ratios</ins></strong></span></p>\n"
                        + "<p><span style=\"color: rgb(0,0,0);font-size: 14px;\">     In mathematics, a ratio is a relationship between two numbers indicating how many times the first number contains the second. For example, if a bowl of fruit contains eight oranges and six lemons, then the ratio of oranges to lemons is eight to six (that is, 8:6, which is equivalent to the ratio 4:3). Similarly, the ratio of lemons to oranges is 6:8 (or 3:4) and the ratio of oranges to the total amount of fruit is 8:14 (or 4:7).</span></p>\n"
                        + "<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Aspect-ratio-4x3.svg/1280px-Aspect-ratio-4x3.svg.png\" alt=\"undefined\" style=\"float:none;height: 250px;width: 300px\"/>\n"
                        + "<p><span style=\"font-size: 14px;\">    Consider a class that has 20 male students and 80 female students. We can think about this in several ways. We could express this simply as the ratio of men to women and write the relationship as 20:80 or 20/80. We can also simplify this by dividing both the numerator and the denominator by a number that divides evenly into both the numerator and the denominator. In this case, we could divide both by 20 to simplify this to a 1:4 ratio (or 1/4 ratio). This indicates that for every man, there are four women.</span></p>\n"
                        + "<p><span style=\"font-size: 14px;\">    We could also consider this from the inverse perspective, i.e., the number of women relative to the number of men; in this case the ratio of women to men is 80/20 which is equivalent to 4 to 1, i.e., there are four women for every man.</span></p>\n"
                        + "<iframe width=\"500\" height=\"400\" src=\"https://www.youtube.com/embed/puku5vUCOcE\" frameBorder=\"0\"></iframe>\n"
                        + "<p></p>\n"
                        + "<p></p>\n");
        chapterService.createChapter(requestChapter);

        requestChapter.setTutorialId(tutorial1);
        requestChapter.setName("Decimals");
        requestChapter.setContent(
                "<p>The decimal numeral system (also called base-ten positional numeral system, and occasionally called denary) is the standard system for denoting integer and non-integer numbers. It is the extension to non-integer numbers of the Hindu–Arabic numeral system.[1] The way of denoting numbers in the decimal system is often referred to as decimal notation.[2]</p>\n"
                        + "<p></p>\n"
                        + "<p>A decimal numeral, or just decimal, or casually decimal number, refers generally to the notation of a number in the decimal numeral system. Decimals may sometimes be identified for containing a decimal separator (for example the \".\" in 10.00 or 3.14159). \"Decimal\" may also refer specifically to the digits after the decimal separator, such as in \"3.14 is the approximation of π to two decimals\".</p>\n"
                        + "<p></p>\n"
                        + "<p>The numbers that may be represented in the decimal system are the decimal fractions, that is the fractions of the form a/10n, where a is an integer, and n is a non-negative integer.</p>\n"
                        + "<p></p>\n"
                        + "<p>The decimal system has been extended to infinite decimals, for representing any real number, by using an infinite sequence of digits after the decimal separator (see Decimal representation). In this context, the decimal numerals with a finite number of non–zero places after the decimal separator are sometimes called terminating decimals. A repeating decimal is an infinite decimal that after some place repeats indefinitely the same sequence of digits (for example 5.123144144144144... = 5.123144).[3] An infinite decimal represents a rational number if and only if it is a repeating decimal or has a finite number of nonzero digits.</p>\n"
                        + "<p></p>\n"
                        + "<p></p>\n"
                        + "<img src=\"https://image01.ipracticemath.com/content/imageslm/decimal/decimal.png\" alt=\"undefined\" style=\"float:none;height: auto;width: auto\"/>\n"
                        + "<p></p>\n"
                        + "<p>Thank you!</p>\n");
        chapterService.createChapter(requestChapter);

        requestChapter.setTutorialId(tutorial1);
        requestChapter.setName("Arithmetic operations");
        requestChapter.setContent(
                "<p></p>\n"
                        + "<iframe width=\"600\" height=\"400\" src=\"https://www.youtube.com/embed/ClYdw4d4OmA\" frameBorder=\"0\"></iframe>\n"
                        + "<p></p>\n"
                        + "<p>So, Arithmetic (from the Greek ἀριθμός arithmos, \"number\" and τική [τέχνη], tiké [téchne], \"art\") is a branch of mathematics that consists of the study of numbers, especially the properties of the traditional operations on them—addition, subtraction, multiplication and division. Arithmetic is an elementary part of number theory, and number theory is considered to be one of the top-level divisions of modern mathematics, along with algebra, geometry, and analysis. The terms arithmetic and higher arithmetic were used until the beginning of the 20th century as synonyms for number theory and are sometimes still used to refer to a wider part of number theory.[1]\uD83D\uDE0B</p>\n");
        chapterService.createChapter(requestChapter);

        requestChapter.setTutorialId(tutorial2);
        requestChapter.setName("Trigonometric identities and equalities");
        requestChapter.setContent(
                "<p><span style=\"color: rgb(226,80,65);\">In mathematics, trigonometric identities are equalities that involve trigonometric functions and are true for every value of the occurring variables where both sides of the equality are defined. Geometrically, these are identities involving certain functions of one or more angles</span>. <strong>They are distinct from triangle identities, which are identities potentially involving angles but also involving side lengths or other lengths of a triangle.</strong></p>\n"
                        + "<p></p>\n"
                        + "<p><em>These identities are useful whenever expressions involving trigonometric functions need to be simplified</em>. <ins>An important application is the integration of non-trigonometric functions: a common technique involves first using the</ins> <del>substitution rule with a trigonometric function, and then simplifying the resulting integral with a trigonometric identity.</del></p>\n"
                        + "<p></p>\n"
                        + "<p>\uD83D\uDE00\uD83D\uDE01\uD83D\uDE0C\uD83D\uDE3A\uD83D\uDC7B\uD83D\uDE07\uD83D\uDE37</p>\n");
        chapterService.createChapter(requestChapter);

        requestChapter.setTutorialId(tutorial3);
        requestChapter.setName("Factors and multiples2");
        requestChapter.setContent("Learn about factors and multiples and how they relate to each other.");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("Fractions2");
        requestChapter.setContent(
                "A fraction (from Latin fractus, \"broken\") represents a part of a whole or, more generally,"
                        + " any number of equal parts. When spoken in everyday English.");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("Decimals2");
        requestChapter.setContent(
                "The decimal numeral system (also called base-ten positional numeral system, and occasionally"
                        + " called denary) is the standard system for denoting integer and non-integer numbers.");
        chapterService.createChapter(requestChapter);

        requestChapter.setTutorialId(tutorial2);

        requestChapter.setName("Trigonometry with right triangles2");
        requestChapter.setContent("Learn about trigonometry with right triangles.");
        chapterService.createChapter(requestChapter);

        requestChapter.setName("Trigonometric equations and identities2");
        requestChapter.setContent("Trigonometric equations and identities.");
        chapterService.createChapter(requestChapter);
    }

    private void addUsers() {
        RostemUser rostemUser1 = new RostemUser("ionut1@gmail.com",
                "$2a$10$xqlzlMI5bBS/i75sjB9bIu1RJ8otRYm3mASRqOgjBJumflJHiicAi", "ionut1", "bio1");
        RostemUser rostemUser2 = new RostemUser("ionut2@gmail.com",
                "$2a$10$xqlzlMI5bBS/i75sjB9bIu1RJ8otRYm3mASRqOgjBJumflJHiicAi", "ionut2", "bio2");

        RostemAdmin admin1 = new RostemAdmin("admin@gmail.com",
                "$2a$10$xqlzlMI5bBS/i75sjB9bIu1RJ8otRYm3mASRqOgjBJumflJHiicAi");

        this.registerService.testAddAdmin(admin1);
        this.registerService.testAddUser(rostemUser1);
        this.registerService.testAddUser(rostemUser2);

    }
}
