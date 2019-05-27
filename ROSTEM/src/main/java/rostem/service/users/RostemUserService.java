package rostem.service.users;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.response.ResponseRostemUser;
import rostem.repository.users.RostemUserRepository;
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
}
