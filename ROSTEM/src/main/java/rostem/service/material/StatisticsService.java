package rostem.service.material;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.response.ResponseStatisticsCategory;
import rostem.model.dto.response.ResponseStatisticsChapter;
import rostem.model.dto.response.ResponseStatisticsUser;
import rostem.model.entities.Category;
import rostem.model.entities.Chapter;
import rostem.model.entities.Comment;
import rostem.model.users.RostemUser;
import rostem.repository.materials.CategoryRepository;
import rostem.repository.materials.ChapterRepository;
import rostem.repository.materials.CommentRepository;
import rostem.repository.users.RostemUserRepository;

@Service
public class StatisticsService {

    private final int STATISTICS_COUNTER = 10;

    private final CommentRepository commentRepository;
    private final ChapterRepository chapterRepository;
    private final RostemUserRepository rostemUserRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public StatisticsService(CommentRepository commentRepository, ChapterRepository chapterRepository,
            RostemUserRepository rostemUserRepository, CategoryRepository categoryRepository) {
        this.commentRepository = commentRepository;
        this.chapterRepository = chapterRepository;
        this.rostemUserRepository = rostemUserRepository;
        this.categoryRepository = categoryRepository;
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

    public List<ResponseStatisticsCategory> getTopCategories() {
        List<Category> categories = categoryRepository.findAll();
        categories.sort((c1, c2) -> {
            if (c1.getUsers().size() == c2.getUsers().size()) {
                return 0;
            }
            return c1.getUsers().size() > c2.getUsers().size() ? -1 : 1;
        });

        return categories.stream().limit(STATISTICS_COUNTER)
                .map(c -> new ResponseStatisticsCategory(getCategoryPercentage(c), c.getName(), c.getUsers().size()))
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
}
