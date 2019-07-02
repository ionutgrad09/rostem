package rostem.service.material;

import static rostem.utils.ApiResponses.CATEGORY_NOT_FOUND;
import static rostem.utils.ApiResponses.TUTORIAL_NOT_FOUND;
import static rostem.utils.ApiResponses.USER_NOT_FOUND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.response.ResponseChapter;
import rostem.model.dto.response.ResponseStatisticsCategory;
import rostem.model.dto.response.ResponseStatisticsChapter;
import rostem.model.dto.response.ResponseStatisticsUser;
import rostem.model.dto.response.ResponseTutorialProgress;
import rostem.model.entities.Category;
import rostem.model.entities.Chapter;
import rostem.model.entities.Comment;
import rostem.model.entities.Tutorial;
import rostem.model.users.RostemUser;
import rostem.repository.materials.CategoryRepository;
import rostem.repository.materials.ChapterRepository;
import rostem.repository.materials.CommentRepository;
import rostem.repository.materials.TutorialRepository;
import rostem.repository.users.RostemUserRepository;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.ChapterMapper;

@Service
public class StatisticsService {

    private final int STATISTICS_COUNTER = 10;

    private final CommentRepository commentRepository;
    private final ChapterRepository chapterRepository;
    private final RostemUserRepository rostemUserRepository;
    private final CategoryRepository categoryRepository;
    private final TutorialRepository tutorialRepository;

    @Autowired
    public StatisticsService(CommentRepository commentRepository, ChapterRepository chapterRepository,
            RostemUserRepository rostemUserRepository, CategoryRepository categoryRepository,
            TutorialRepository tutorialRepository) {
        this.commentRepository = commentRepository;
        this.chapterRepository = chapterRepository;
        this.rostemUserRepository = rostemUserRepository;
        this.categoryRepository = categoryRepository;
        this.tutorialRepository = tutorialRepository;
    }

    @Transactional
    public List<ResponseStatisticsChapter> getTopChapters() {
        List<Chapter> chapters = chapterRepository.findAll();
        chapters.sort((c1, c2) -> {
            int c1Score = getChapterScore(c1);
            int c2Score = getChapterScore(c2);

            if (c1Score == c2Score) {
                return 0;
            }
            return c1Score > c2Score ? -1 : 1;
        });

        return chapters.stream().limit(STATISTICS_COUNTER)
                .map(c -> new ResponseStatisticsChapter(c.getUserLikes().size(), c.getTodoUserList().size(),
                        c.getDoneUserList().size(), c.getComments().size(), c.getName())).collect(
                        Collectors.toList());
    }

    private int getChapterScore(Chapter chapter) {
        return chapter.getUserLikes().size() + chapter.getTodoUserList().size() + chapter.getDoneUserList().size()
                + chapter.getComments().size();
    }

    @Transactional
    public List<ResponseStatisticsCategory> getTopCategories() {
        List<Category> categories = categoryRepository.findAll();
        categories.sort((c1, c2) -> {
            if (c1.getUsers().size() == c2.getUsers().size()) {
                return 0;
            }
            return c1.getUsers().size() > c2.getUsers().size() ? -1 : 1;
        });

        return categories.stream().limit(STATISTICS_COUNTER)
                .map(c -> new ResponseStatisticsCategory(getCategoryPercentage(c), c.getTutorials().size(), c.getName(), c.getUsers().size()))
                .collect(
                        Collectors.toList());
    }

    private float getCategoryPercentage(Category category) {
        return (float) ((categoryRepository.findCategoryByName(category.getName()).getUsers().size() * 100)
                / rostemUserRepository.findAll().size());
    }

    @Transactional
    public List<ResponseStatisticsUser> getTopUsers() {
        List<RostemUser> rostemUsers = rostemUserRepository.findAll();
        rostemUsers.sort((r1, r2) -> {
            int r1Score = getUserScore(r1);
            int r2Score = getUserScore(r2);

            if (r1Score == r2Score) {
                return 0;
            }
            return r1Score > r2Score ? -1 : 1;
        });

        return rostemUsers.stream().limit(STATISTICS_COUNTER)
                .map(c -> new ResponseStatisticsUser(getNoComments(c), c.getTodoChapters().size(),
                        c.getDoneChapters().size(), c.getLikedChapters().size(), c.getFavoriteCategories().size(),
                        c.getEmail()))
                .collect(
                        Collectors.toList());
    }

    private int getNoComments(RostemUser user) {
        List<Comment> comments = commentRepository.findAll();
        int counter = 0;
        for (Comment comment : comments) {
            if (comment.getEmail().equals(user.getEmail())) {
                counter++;
            }
        }
        return counter;
    }

    private int getUserScore(RostemUser user) {
        return user.getFavoriteCategories().size() + user.getLikedChapters().size() + user.getDoneChapters().size() +
                user.getTodoChapters().size() + getNoComments(user);
    }

    @Transactional
    public List<ResponseChapter> getRandomChapters(String email) {
        if (findUserByEmail(email)) {
            RostemUser rostemUser = rostemUserRepository.findByEmail(email);
            List<Category> categories = rostemUser.getFavoriteCategories();
            List<Tutorial> tutorials = new ArrayList<>();

            for (Category category : categories) {
                List<ResponseTutorialProgress> responseTutorialProgresses = this
                        .getTutorialProgress(email, category.getName());
                for (ResponseTutorialProgress responseTutorialProgress : responseTutorialProgresses) {
                    if (responseTutorialProgress.getPercentage() < 60) {
                        tutorials.add(tutorialRepository.findTutorialByName(responseTutorialProgress.getName()));
                    }
                }
            }

            return this.getChapterSuggestions(email, rostemUser.getDoneChapters(), tutorials);
        } else {
            throw new RostemException(USER_NOT_FOUND);
        }

    }

    private List<ResponseChapter> getChapterSuggestions(String email, List<Chapter> doneChapters,
            List<Tutorial> tutorials) {
        List<Chapter> finalChapters = new ArrayList<>();

        for (Tutorial tutorial : tutorials) {
            for (Chapter chapter : tutorial.getChapters()) {
                boolean notExist = true;
                for (Chapter doneChapter : doneChapters) {
                    if (chapter.getId().equals(doneChapter.getId())) {
                        notExist = false;
                    }
                }
                if (notExist) {
                    finalChapters.add(chapter);
                }
            }
        }
        Collections.shuffle(finalChapters);
        List<ResponseChapter> responseChapters = finalChapters.stream().limit(3).map(ChapterMapper::map)
                .collect(Collectors.toList());
        this.addCategoryName(responseChapters);
        this.markedAsTodo(email, responseChapters);
        return responseChapters;
    }

    public void markedAsTodo(String email, List<ResponseChapter> responseChapters) {
        List<Chapter> todoChapters = rostemUserRepository.findByEmail(email).getTodoChapters();

        for (ResponseChapter responseChapter : responseChapters) {
            for (Chapter chapter : todoChapters) {
                if (responseChapter.getId().equals(chapter.getId())) {
                    responseChapter.setTodo(true);
                }
            }
        }
    }

    public void addCategoryName(List<ResponseChapter> responseChapters) {
        try {
            responseChapters.forEach(chapter -> {
                chapter.setCategoryName(
                        tutorialRepository.findTutorialByName(chapter.getTutorialName()).getCategory().getName());
            });
        } catch (Exception e) {
            throw new RostemException(TUTORIAL_NOT_FOUND);
        }
    }


    @Transactional
    public List<ResponseTutorialProgress> getTutorialProgress(String email, String categoryName) {
        List<Tutorial> tutorials = checkCategory(categoryName).getTutorials();
        List<Chapter> doneChapters = checkUser(email).getDoneChapters();

        List<ResponseTutorialProgress> responseTutorialProgresses = new ArrayList<>();

        for (Tutorial tutorial : tutorials) {
            final int noChapters = tutorial.getChapters().size();

            ResponseTutorialProgress responseTutorialProgress = new ResponseTutorialProgress();
            responseTutorialProgress.setName(tutorial.getName());
            responseTutorialProgress.setTotalChapters(noChapters);

            int counter = 0;
            for (Chapter doneChapter : doneChapters) {
                for (Chapter chapter : tutorial.getChapters()) {
                    if (doneChapter.getId().equals(chapter.getId())) {
                        counter++;
                    }
                }
            }
            responseTutorialProgress.setDoneChapters(counter);
            responseTutorialProgress.setPercentage(
                    noChapters == 0 ? 0 : (float) (counter * 100) / noChapters);
            responseTutorialProgresses.add(responseTutorialProgress);
        }

        return responseTutorialProgresses;
    }

    private RostemUser checkUser(String email) {
        if (!findUserByEmail(email)) {
            throw new RostemException(USER_NOT_FOUND);
        } else {
            return rostemUserRepository.findByEmail(email);
        }
    }

    private Category checkCategory(String categoryName) {
        if (!findCategoryByName(categoryName)) {
            throw new RostemException(CATEGORY_NOT_FOUND);
        } else {
            return categoryRepository.findCategoryByName(categoryName);
        }
    }

    private boolean findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name) != null;
    }

    private boolean findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id) != null;
    }

    private boolean findUserByEmail(String email) {
        return rostemUserRepository.findByEmail(email) != null;
    }
}
