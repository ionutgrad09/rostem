package rostem.utils;

public class ApiResponses {


    /*
     * GENERAL
     */
    public final static String REQUEST_OK = "OK";

    /*
     * REGISTER
     */
    public final static String ACCOUNT_REGISTERED_NOT_ACTIVATED = "The account is already in use, but it has to be activated.";
    public final static String USER_ALREADY_REGISTERED = "The user is already registered.";
    public final static String INVALID_ACCOUNT_KEY = "Invalid account key";
    public final static String EMAIL_SERVICE_NOT_AVAILABLE = "The email service is not available.";

    /*
     * CATEGORY
     */
    public final static String CATEGORY_NOT_FOUND = "The category does not exist.";
    public final static String CATEGORY_NOT_FOUND_FOR_USER = "Category not found for the given user.";
    public final static String CATEGORY_ALREADY_EXISTS = "Category name must be unique.";

    /*
     * TUTORIAL
     */
    public final static String TUTORIAL_NOT_FOUND = "The tutorial does not exist.";
    public final static String TUTORIAL_ALREADY_EXISTS = "Tutorial name must be unique.";

    /*
     * CHAPTER
     */
    public final static String CHAPTER_NOT_FOUND = "The chapter does not exist.";
    public final static String CHAPTER_ALREADY_EXISTS = "Chapter name must be unique.";

    /*
     * USER
     */
    public final static String USER_NOT_FOUND = "User not found.";
    public final static String USER_NOT_FOUND_FOR_CATEGORY = "User not found for the given category.";
    public final static String USER_NOT_FOUND_FOR_CHAPTER = "User not found for the given chapter.";


    /*
     * TO_DO
     */
    public final static String CHAPTER_ALREADY_MARKED_AS_TODO = "The chapters is already marked as TODO.";
    public final static String CHAPTER_ALREADY_MARKED_AS_DONE = "The chapters is already marked as DONE.";


    public final static String HTTP_REQUEST_INVALID_VALUE = "The request cannot be fulfilled due to bad syntax or invalid parameters.";

}
