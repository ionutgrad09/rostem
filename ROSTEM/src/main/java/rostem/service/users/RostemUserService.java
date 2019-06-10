package rostem.service.users;

import static rostem.utils.ApiResponses.USER_NOT_FOUND;
import static rostem.utils.mapper.RostemUserMapper.map;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestRostemUser;
import rostem.model.dto.response.ResponseRostemUser;
import rostem.model.users.RostemUser;
import rostem.repository.users.RostemUserRepository;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.RostemUserMapper;

@Service
public class RostemUserService {

    private final static Logger logger = LoggerFactory.getLogger(RostemUserService.class);

    private final RostemUserRepository rostemUserRepository;

    @Autowired
    public RostemUserService(RostemUserRepository rostemUserRepository) {
        this.rostemUserRepository = rostemUserRepository;
    }

    public List<ResponseRostemUser> getAllUsers() {
        logger.info("[ADMIN] Get all users");

        return rostemUserRepository.findAll()
                .stream()
                .map(RostemUserMapper::map)
                .collect(Collectors.toList());
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
            return map(rostemUserRepository.findByEmail(email));
        } else {
            throw new RostemException(USER_NOT_FOUND);
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
