package rostem.utils.ResponseBuilder;

public class ExceptionMessages {

    //TODO CHANGE THIS
    /**
     * Course
     */

    // Grade structure:

    public final static String ALREADY_HAS_GRADE_STRUCTURE = "alreadyHasGradeStructure";
    public final static String NOT_FOUND_GRADE_STRUCTURE = "notFoundGradeStructure";
    public final static String INVALID_GRADE_STRUCTURE_NODE_TYPE = "invalidGradeStructureType";

    // Attendance structure:

    public final static String ALREADY_HAS_ATTENDANCE_STRUCTURE = "alreadyHasAttendanceStructure";
    public final static String NOT_FOUND_ATTENDANCE_STRUCTURE = "notFoundAttendanceStructure";
    public final static String INVALID_ATTENDANCE_STRUCTURE_NODE_TYPE = "invalidAttendanceStructureType";

    // Grade structure and attendance structure:

    public final static String NODE_HAS_MULTIPLE_PARENTS = "nodeHasMultipleParents";
    public final static String MAXIMUM_RECURSION_DEPTH_REACHED = "passedMaximumRecursionDepth";
    public final static String INVALID_GRADE_DATA = "inputGradeIsInvalid";
}
