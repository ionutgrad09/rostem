package rostem.service.material;

import static rostem.utils.ApiResponses.CHAPTER_ALREADY_EXISTS;
import static rostem.utils.ApiResponses.CHAPTER_NOT_FOUND;
import static rostem.utils.ApiResponses.TUTORIAL_NOT_FOUND;
import static rostem.utils.ApiResponses.USER_NOT_FOUND;
import static rostem.utils.ApiResponses.USER_NOT_FOUND_FOR_CHAPTER;
import static rostem.utils.mapper.ChapterMapper.map;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestActionChapter;
import rostem.model.dto.request.RequestChapter;
import rostem.model.dto.response.ResponseChapter;
import rostem.model.material.Chapter;
import rostem.model.users.RostemUser;
import rostem.repository.materials.ChapterRepository;
import rostem.repository.materials.TutorialRepository;
import rostem.repository.users.RostemUserRepository;
import rostem.utils.ResponseBuilder.Response;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.ChapterMapper;

@Service
public class ChapterService {

    private final static Logger logger = LoggerFactory.getLogger(ChapterService.class);

    private TutorialRepository tutorialRepository;
    private ChapterRepository chapterRepository;
    private final RostemUserRepository rostemUserRepository;

    @Autowired
    public ChapterService(TutorialRepository tutorialRepository, ChapterRepository chapterRepository,
            RostemUserRepository rostemUserRepository) {
        this.tutorialRepository = tutorialRepository;
        this.chapterRepository = chapterRepository;
        this.rostemUserRepository = rostemUserRepository;
    }

    public List<ResponseChapter> getAllChapters() {
        logger.info("[CHAPTER] Get all chapters.");

        return chapterRepository.findAll().stream().map(ChapterMapper::map).collect(Collectors.toList());
    }

    public List<ResponseChapter> getChaptersForTutorial(RequestActionChapter requestActionChapter) {
        final Long tutorialId = requestActionChapter.getTutorialId();
        logger.info("[CHAPTER] Trying to get all chapters for tutorial " + tutorialId);

        if (!findTutorialById(tutorialId)) {
            throw new RostemException(TUTORIAL_NOT_FOUND);
        } else {
            List<ResponseChapter> responseChapters = tutorialRepository.findTutorialById(tutorialId).get().getChapters()
                    .stream()
                    .map(ChapterMapper::map)
                    .collect(Collectors.toList());

            markedAsDone(requestActionChapter.getEmail(), responseChapters);
            markedAsTodo(requestActionChapter.getEmail(), responseChapters);

            return responseChapters;
        }
    }


    private void markedAsDone(String email, List<ResponseChapter> responseChapters) {
        Set<Chapter> doneChapters = rostemUserRepository.findByEmail(email).getDoneChapters();

        for (ResponseChapter responseChapter : responseChapters) {
            for (Chapter chapter : doneChapters) {
                if (responseChapter.getId() == chapter.getId()) {
                    responseChapter.setDone(true);
                }
            }
        }
    }

    private void markedAsTodo(String email, List<ResponseChapter> responseChapters) {
        Set<Chapter> todoChapters = rostemUserRepository.findByEmail(email).getTodoChapters();

        for (ResponseChapter responseChapter : responseChapters) {
            for (Chapter chapter : todoChapters) {
                if (responseChapter.getId() == chapter.getId()) {
                    responseChapter.setTodo(true);
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
                chapter.setTutorial(tutorialRepository.findTutorialById(tutorialId).get());
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
                chapterRepository.deleteById(id);
            }
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
            chapter.setTutorial(tutorialRepository.findTutorialById(requestChapter.getTutorialId()).get());
            return map(chapterRepository.save(chapter));
        }
    }

    public List<ResponseChapter> getLatestChapters(int count) {
        logger.info("[CHAPTER] Getting latest created chapters..");

        return chapterRepository.findAllByOrderByCreationDateDesc().stream().limit(count).map(ChapterMapper::map)
                .collect(Collectors.toList());
    }

    public void markChapterAsTodo(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to set a chapter as todo for user " + email);

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id).get();
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);

        chapter.getTodoUserList().add(rostemUser);
        rostemUser.getTodoChapters().add(chapter);
        chapterRepository.save(chapter);
        rostemUserRepository.save(rostemUser);
    }

    public void markChapterAsDone(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to set a chapter as done for user " + email);

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id).get();
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
    public void unmarkTodoChapter(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to unmark a todo chapter from user " + email + " TODO list.");

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id).get();
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);
        deleteTodoChapterFromUser(chapter, rostemUser);
        deleteUserFromTodoChapter(chapter, rostemUser);
    }

    @Transactional
    public void unmarkDoneChapter(String email, Long id) {
        logger.info("[USER_CHAPTER] Trying to unmark a done chapter from user " + email + " TODO list.");

        checkForUserAndChapter(email, id);
        Chapter chapter = chapterRepository.findChapterById(id).get();
        RostemUser rostemUser = rostemUserRepository.findByEmail(email);
        deleteDoneChapterFromUser(chapter, rostemUser);
        deleteUserFromDoneChapter(chapter, rostemUser);
    }


    private void deleteTodoChapterFromUser(Chapter chapter, RostemUser rostemUser) {
        Set<Chapter> userTodoChapters = rostemUser.getTodoChapters();
        if (userTodoChapters.stream().anyMatch(x -> x.getId() == chapter.getId())) {
            userTodoChapters.remove(chapter);
            rostemUser.setTodoChapters(userTodoChapters);
            rostemUserRepository.save(rostemUser);
        } else {
            throw new RostemException(CHAPTER_NOT_FOUND);
        }
    }

    private void deleteUserFromTodoChapter(Chapter chapter, RostemUser rostemUser) {
        Set<RostemUser> todoChaptersUser = chapter.getTodoUserList();
        if (todoChaptersUser.stream().anyMatch(x -> x.getEmail().equals(rostemUser.getEmail()))) {
            todoChaptersUser.remove(rostemUser);
            chapter.setTodoUserList(todoChaptersUser);
            chapterRepository.save(chapter);
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    private void deleteDoneChapterFromUser(Chapter chapter, RostemUser rostemUser) {
        Set<Chapter> userDoneChapters = rostemUser.getDoneChapters();
        if (userDoneChapters.stream().anyMatch(x -> x.getId() == chapter.getId())) {
            userDoneChapters.remove(chapter);
            rostemUser.setDoneChapters(userDoneChapters);
            rostemUserRepository.save(rostemUser);
        } else {
            throw new RostemException(CHAPTER_NOT_FOUND);
        }
    }

    private void deleteUserFromDoneChapter(Chapter chapter, RostemUser rostemUser) {
        Set<RostemUser> doneChaptersUser = chapter.getDoneUserList();
        if (doneChaptersUser.stream().anyMatch(x -> x.getEmail().equals(rostemUser.getEmail()))) {
            doneChaptersUser.remove(rostemUser);
            chapter.setDoneUserList(doneChaptersUser);
            chapterRepository.save(chapter);
        } else {
            throw new RostemException(USER_NOT_FOUND_FOR_CHAPTER);
        }
    }

    private boolean findUserByEmail(String email) {
        return rostemUserRepository.findByEmail(email) != null;
    }

    private boolean findTutorialById(Long id) {
        return tutorialRepository.findTutorialById(id).isPresent();
    }

    private boolean findChapterById(Long id) {
        return chapterRepository.findChapterById(id).isPresent();
    }

    private boolean findChapterByName(String name) {
        return chapterRepository.findChapterByName(name).isPresent();
    }
}
