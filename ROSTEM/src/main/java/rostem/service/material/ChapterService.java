package rostem.service.material;

import static rostem.utils.ApiResponses.CATEGORY_NOT_FOUND;
import static rostem.utils.ApiResponses.CHAPTER_ALREADY_EXISTS;
import static rostem.utils.ApiResponses.CHAPTER_NOT_FOUND;
import static rostem.utils.ApiResponses.TUTORIAL_NOT_FOUND;
import static rostem.utils.ApiResponses.USER_NOT_FOUND;
import static rostem.utils.ApiResponses.USER_NOT_FOUND_FOR_CHAPTER;
import static rostem.utils.mapper.ChapterMapper.map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestActionChapter;
import rostem.model.dto.request.RequestChapter;
import rostem.model.dto.request.RequestComment;
import rostem.model.dto.request.RequestRecentPosts;
import rostem.model.dto.response.ResponseChapter;
import rostem.model.dto.response.ResponseComment;
import rostem.model.entities.Chapter;
import rostem.model.entities.Comment;
import rostem.model.users.RostemUser;
import rostem.repository.materials.ChapterRepository;
import rostem.repository.materials.CommentRepository;
import rostem.repository.materials.TutorialRepository;
import rostem.repository.users.RostemUserRepository;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.ChapterMapper;
import rostem.utils.mapper.CommentsMapper;

@Service
public class ChapterService {

    private final static Logger logger = LoggerFactory.getLogger(ChapterService.class);

    private final TutorialRepository tutorialRepository;
    private final ChapterRepository chapterRepository;
    private final RostemUserRepository rostemUserRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ChapterService(TutorialRepository tutorialRepository, ChapterRepository chapterRepository,
            RostemUserRepository rostemUserRepository, CommentRepository commentRepository) {
        this.tutorialRepository = tutorialRepository;
        this.chapterRepository = chapterRepository;
        this.rostemUserRepository = rostemUserRepository;
        this.commentRepository = commentRepository;
    }

    public List<ResponseChapter> getAllChapters() {
        logger.info("[CHAPTER] Get all chapters.");

        return chapterRepository.findAll().stream().map(ChapterMapper::map).collect(Collectors.toList());
    }

    public List<ResponseChapter> getLikedChapters(String email) {
        logger.info("[CHAPTER] Get all liked chapters by user: " + email);

        if (this.findUserByEmail(email)) {
            List<ResponseChapter> responseChapters = rostemUserRepository.findByEmail(email).getLikedChapters().stream()
                    .map(ChapterMapper::map)
                    .collect(Collectors.toList());

            markedAsDone(email, responseChapters);
            markedAsTodo(email, responseChapters);
            markedAsLiked(email, responseChapters);

            return responseChapters;
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    public List<ResponseChapter> getTodoChapters(String email) {
        logger.info("[CHAPTER] Get all todo chapters for user: " + email);

        if (this.findUserByEmail(email)) {
            List<ResponseChapter> responseChapters = rostemUserRepository.findByEmail(email).getTodoChapters().stream()
                    .map(ChapterMapper::map)
                    .collect(Collectors.toList());

            markedAsDone(email, responseChapters);
            markedAsTodo(email, responseChapters);
            markedAsLiked(email, responseChapters);

            return responseChapters;
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    public List<ResponseChapter> getDoneChapters(String email) {
        logger.info("[CHAPTER] Get all done chapters for user: " + email);

        if (this.findUserByEmail(email)) {
            List<ResponseChapter> responseChapters = rostemUserRepository.findByEmail(email).getDoneChapters().stream()
                    .map(ChapterMapper::map)
                    .collect(Collectors.toList());

            markedAsDone(email, responseChapters);
            markedAsTodo(email, responseChapters);
            markedAsLiked(email, responseChapters);

            return responseChapters;
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    public List<ResponseChapter> getChaptersForTutorial(RequestActionChapter requestActionChapter) {
        final Long tutorialId = requestActionChapter.getTutorialId();
        logger.info("[CHAPTER] Trying to get all chapters for tutorial " + tutorialId);

        if (!findTutorialById(tutorialId)) {
            throw new RostemException(TUTORIAL_NOT_FOUND);
        } else {
            List<ResponseChapter> responseChapters = tutorialRepository.findTutorialById(tutorialId).getChapters()
                    .stream()
                    .map(ChapterMapper::map)
                    .collect(Collectors.toList());

            markedAsDone(requestActionChapter.getEmail(), responseChapters);
            markedAsTodo(requestActionChapter.getEmail(), responseChapters);
            markedAsLiked(requestActionChapter.getEmail(), responseChapters);

            return responseChapters;
        }
    }

    private void markedAsDone(String email, List<ResponseChapter> responseChapters) {
        List<Chapter> doneChapters = rostemUserRepository.findByEmail(email).getDoneChapters();

        for (ResponseChapter responseChapter : responseChapters) {
            for (Chapter chapter : doneChapters) {
                if (responseChapter.getId().equals(chapter.getId())) {
                    responseChapter.setDone(true);
                }
            }
        }
    }

    private void markedAsTodo(String email, List<ResponseChapter> responseChapters) {
        List<Chapter> todoChapters = rostemUserRepository.findByEmail(email).getTodoChapters();

        for (ResponseChapter responseChapter : responseChapters) {
            for (Chapter chapter : todoChapters) {
                if (responseChapter.getId().equals(chapter.getId())) {
                    responseChapter.setTodo(true);
                }
            }
        }
    }

    private void markedAsLiked(String email, List<ResponseChapter> responseChapters) {
        List<Chapter> likedChapters = rostemUserRepository.findByEmail(email).getLikedChapters();

        for (ResponseChapter responseChapter : responseChapters) {
            for (Chapter chapter : likedChapters) {
                if (responseChapter.getId().equals(chapter.getId())) {
                    responseChapter.setLiked(true);
                }
            }
        }
    }

    public ResponseChapter createChapter(RequestChapter requestChapter) {
        final Long tutorialId = requestChapter.getTutorialId();
        final String name = requestChapter.getName();
        logger.info("[CHAPTER] Trying to add a new chapter " + name);

        if (!findTutorialById(tutorialId)) {
            throw new RostemException(TUTORIAL_NOT_FOUND);
        } else {
            if (findChapterByName(name)) {
                throw new RostemException(CHAPTER_ALREADY_EXISTS);
            } else {
                Chapter chapter = map(requestChapter);
                chapter.setTutorial(tutorialRepository.findTutorialById(tutorialId));
                return map(chapterRepository.save(chapter));
            }
        }
    }

    @Transactional
    public void deleteChapters(List<Long> ids) {
        for (Long id : ids) {
            logger.info("[CHAPTER] Trying to delete chapter " + id);

            if (!findChapterById(id)) {
                throw new RostemException(CHAPTER_NOT_FOUND);
            } else {
                Chapter chapter = chapterRepository.findChapterById(id);
                deleteFromUserTodoAndDone(chapter);
                chapterRepository.deleteById(id);
            }
        }
    }

    private void deleteFromUserTodoAndDone(Chapter chapter) {
        List<RostemUser> rostemUsers = rostemUserRepository.findAll();

        for (RostemUser rostemUser : rostemUsers) {
            rostemUser.getDoneChapters().remove(chapter);
            rostemUser.getTodoChapters().remove(chapter);
            rostemUserRepository.save(rostemUser);
        }
    }

    @Transactional
    public ResponseChapter updateChapter(Long id, RequestChapter requestChapter) {
        logger.info("[CHAPTER] Trying to update chapter " + id);

        if (!findChapterById(id)) {
            throw new RostemException(CHAPTER_NOT_FOUND);
        } else {
            Chapter chapter = map(requestChapter);
            chapter.setId(id);
            chapter.setTutorial(tutorialRepository.findTutorialById(requestChapter.getTutorialId()));
            return map(chapterRepository.save(chapter));
        }
    }

    public List<ResponseChapter> getLatestChapters(RequestRecentPosts requestRecentPosts) {
        logger.info("[CHAPTER] Getting latest created chapters..");

        List<ResponseChapter> responseChapters = chapterRepository.findAllByOrderByCreationDateDesc()
                .stream()
                .limit(requestRecentPosts.getCounter())
                .map(ChapterMapper::map)
                .collect(Collectors.toList());

        markedAsDone(requestRecentPosts.getEmail(), responseChapters);
        markedAsTodo(requestRecentPosts.getEmail(), responseChapters);

        return responseChapters;
    }

    public void likeChapter(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to set a chapter as liked by user " + email);

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id);
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);

        chapter.getUserLikes().add(rostemUser);
        rostemUser.getLikedChapters().add(chapter);
        chapterRepository.save(chapter);
        rostemUserRepository.save(rostemUser);
    }

    public void markChapterAsTodo(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to set a chapter as todo for user " + email);

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id);
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);

        chapter.getTodoUserList().add(rostemUser);
        rostemUser.getTodoChapters().add(chapter);
        chapterRepository.save(chapter);
        rostemUserRepository.save(rostemUser);
    }

    public void markChapterAsDone(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to set a chapter as done for user " + email);

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id);
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);

        chapter.getDoneUserList().add(rostemUser);
        rostemUser.getDoneChapters().add(chapter);
        chapterRepository.save(chapter);
        rostemUserRepository.save(rostemUser);
    }

    private void checkForUserAndChapter(String email, Long id) {
        if (!findUserByEmail(email)) {
            throw new RostemException(USER_NOT_FOUND);
        } else if (!findChapterById(id)) {
            throw new RostemException(CHAPTER_NOT_FOUND);
        }
    }

    @Transactional
    public void dislikeChapter(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to dislike a chapter for user " + email);

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id);
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);
        deleteLikeChapterFromUser(chapter, rostemUser);
        deleteUserFromLikeChapter(chapter, rostemUser);
    }

    private void deleteLikeChapterFromUser(Chapter chapter, RostemUser rostemUser) {
        List<Chapter> userLikes = rostemUser.getLikedChapters();
        if (userLikes.stream().anyMatch(x -> x.getId().equals(chapter.getId()))) {
            userLikes.remove(chapter);
            rostemUser.setLikedChapters(userLikes);
            rostemUserRepository.save(rostemUser);
        } else {
            throw new RostemException(CHAPTER_NOT_FOUND);
        }
    }

    private void deleteUserFromLikeChapter(Chapter chapter, RostemUser rostemUser) {
        List<RostemUser> userLikes = chapter.getUserLikes();
        if (userLikes.stream().anyMatch(x -> x.getEmail().equals(rostemUser.getEmail()))) {
            userLikes.remove(rostemUser);
            chapter.setUserLikes(userLikes);
            chapterRepository.save(chapter);
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    @Transactional
    public void unmarkTodoChapter(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to unmark a todo chapter from user " + email + " TODO list.");

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id);
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);
        deleteTodoChapterFromUser(chapter, rostemUser);
        deleteUserFromTodoChapter(chapter, rostemUser);
    }

    @Transactional
    public void unmarkDoneChapter(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to unmark a done chapter from user " + email + " TODO list.");

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id);
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);
        deleteDoneChapterFromUser(chapter, rostemUser);
        deleteUserFromDoneChapter(chapter, rostemUser);
    }


    private void deleteTodoChapterFromUser(Chapter chapter, RostemUser rostemUser) {
        List<Chapter> userTodoChapters = rostemUser.getTodoChapters();
        if (userTodoChapters.stream().anyMatch(x -> x.getId().equals(chapter.getId()))) {
            userTodoChapters.remove(chapter);
            rostemUser.setTodoChapters(userTodoChapters);
            rostemUserRepository.save(rostemUser);
        } else {
            throw new RostemException(CHAPTER_NOT_FOUND);
        }
    }

    private void deleteUserFromTodoChapter(Chapter chapter, RostemUser rostemUser) {
        List<RostemUser> todoChaptersUser = chapter.getTodoUserList();
        if (todoChaptersUser.stream().anyMatch(x -> x.getEmail().equals(rostemUser.getEmail()))) {
            todoChaptersUser.remove(rostemUser);
            chapter.setTodoUserList(todoChaptersUser);
            chapterRepository.save(chapter);
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    private void deleteDoneChapterFromUser(Chapter chapter, RostemUser rostemUser) {
        List<Chapter> userDoneChapters = rostemUser.getDoneChapters();
        if (userDoneChapters.stream().anyMatch(x -> x.getId() == chapter.getId())) {
            userDoneChapters.remove(chapter);
            rostemUser.setDoneChapters(userDoneChapters);
            rostemUserRepository.save(rostemUser);
        } else {
            throw new RostemException(CHAPTER_NOT_FOUND);
        }
    }

    private void deleteUserFromDoneChapter(Chapter chapter, RostemUser rostemUser) {
        List<RostemUser> doneChaptersUser = chapter.getDoneUserList();
        if (doneChaptersUser.stream().anyMatch(x -> x.getEmail().equals(rostemUser.getEmail()))) {
            doneChaptersUser.remove(rostemUser);
            chapter.setDoneUserList(doneChaptersUser);
            chapterRepository.save(chapter);
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    @Transactional
    public List<ResponseComment> getCommentsForChapter(Long chapterId) {
        logger.info("[CHAPTER] Trying to get all comments for chapter " + chapterId);

        if (!findChapterById(chapterId)) {
            throw new RostemException(CHAPTER_NOT_FOUND);
        } else {
            List<Comment> comments = chapterRepository.findChapterById(chapterId).getComments();
            List<ResponseComment> responseComments = new ArrayList<>();
            for (Comment comment : comments) {
                ResponseComment responseComment = CommentsMapper.map(comment);
                responseComment.setUsername(getUsernameForEmail(comment.getEmail()));
                responseComments.add(responseComment);
            }
            return responseComments;
        }
    }

    private String getUsernameForEmail(String email) {
        return rostemUserRepository.findByEmail(email).getUsername();
    }

    @Transactional
    public ResponseComment addComment(RequestComment requestComment) {
        final Long chapterId = requestComment.getChapterId();

        if (!findChapterById(chapterId)) {
            throw new RostemException(CATEGORY_NOT_FOUND);
        } else if (findUserByEmail(requestComment.getEmail())) {
            RostemUser rostemUser = rostemUserRepository.findByEmail(requestComment.getEmail());
            Comment comment = CommentsMapper.map(requestComment);
            comment.setChapter(chapterRepository.findChapterById(chapterId));
            ResponseComment responseComment = CommentsMapper.map(commentRepository.save(comment));
            responseComment.setUsername(rostemUser.getUsername());
            return responseComment;
        } else {
            throw new RostemException(USER_NOT_FOUND);
        }
    }

    private boolean findUserByEmail(String email) {
        return rostemUserRepository.findByEmail(email) != null;
    }

    private boolean findTutorialById(Long id) {
        return tutorialRepository.findTutorialById(id) != null;
    }

    private boolean findChapterById(Long id) {
        return chapterRepository.findChapterById(id) != null;
    }

    private boolean findChapterByName(String name) {
        return chapterRepository.findChapterByName(name) != null;
    }
}
