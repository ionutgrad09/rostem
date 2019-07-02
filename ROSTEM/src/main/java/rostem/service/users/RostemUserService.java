package rostem.service.users;

import static rostem.utils.ApiResponses.USER_NOT_FOUND;
import static rostem.utils.mapper.RostemUserMapper.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestRostemUser;
import rostem.model.dto.response.ResponseComment;
import rostem.model.dto.response.ResponseRostemUser;
import rostem.model.dto.response.ResponseTutorialProgress;
import rostem.model.entities.Category;
import rostem.model.users.RostemUser;
import rostem.repository.materials.CategoryRepository;
import rostem.repository.users.RostemUserRepository;
import rostem.service.material.CategoryService;
import rostem.service.material.StatisticsService;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.RostemUserMapper;

@Service
public class RostemUserService<T extends Serializable> {

    private final static Logger logger = LoggerFactory.getLogger(RostemUserService.class);

    private final RostemUserRepository rostemUserRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticsService statisticsService;

    @Autowired
    public RostemUserService(RostemUserRepository rostemUserRepository, CategoryRepository categoryRepository,
            StatisticsService statisticsService) {
        this.rostemUserRepository = rostemUserRepository;
        this.categoryRepository = categoryRepository;
        this.statisticsService = statisticsService;
    }

    public List<ResponseRostemUser> getAllUsers() {
        logger.info("[ADMIN] Get all users");

        List<ResponseRostemUser>  rostemUsers = rostemUserRepository.findAll()
                .stream()
                .map(RostemUserMapper::map)
                .collect(Collectors.toList());
        rostemUsers.forEach(rostemUser -> setBadgesForUser(rostemUser.getEmail(), (T) rostemUser, false));
        return rostemUsers;
    }

    @Transactional
    public void deleteUserByEmail(List<String> emails) {
        for (String email : emails) {
            logger.info("[ADMIN] Trying to delete user with email: " + email);
            rostemUserRepository.deleteByEmail(email);
            logger.info("[ADMIN] User with email " + email + " was deleted successfully");
        }
    }

    public ResponseRostemUser getUserByEmail(String email) {
        if (isUser(email)) {
            ResponseRostemUser responseRostemUser = map(rostemUserRepository.findByEmail(email));
            setBadgesForUser(email, (T) responseRostemUser, false);
            return responseRostemUser;
        } else {
            throw new RostemException(USER_NOT_FOUND);
        }
    }


    public void setBadgesForUser(String email, T responseObject, boolean forComment) {
        List<Category> categories = this.categoryRepository.findAll();
        List<String> badges = new ArrayList<>();

        for (Category category : categories) {
            List<ResponseTutorialProgress> responseTutorialProgresses = this.statisticsService
                    .getTutorialProgress(email, category.getName());
            for (ResponseTutorialProgress responseTutorialProgress : responseTutorialProgresses) {
                if (responseTutorialProgress.getPercentage() == 100) {
                    badges.add(responseTutorialProgress.getName());
                }
            }
        }

        if(forComment) {
            ResponseComment responseComment = (ResponseComment) responseObject;
            responseComment.setBadges(badges);
        } else {
            ResponseRostemUser responseRostemUser = (ResponseRostemUser) responseObject;
            responseRostemUser.setBadges(badges);
        }

    }

    @Transactional
    public void updateUser(RequestRostemUser requestRostemUser) {
        if (isUser(requestRostemUser.getEmail())) {
            RostemUser rostemUser = rostemUserRepository.findByEmail(requestRostemUser.getEmail());
            rostemUser.setUsername(requestRostemUser.getNewUsername());
            rostemUser.setBio(requestRostemUser.getNewBio());
            // rostemUser.setPhoto(requestRostemUser.getNewPhoto());
            rostemUserRepository.save(rostemUser);
        } else {
            throw new RostemException(USER_NOT_FOUND);
        }
    }

    private boolean isUser(String email) {
        return rostemUserRepository.findByEmail(email) != null;
    }
}
