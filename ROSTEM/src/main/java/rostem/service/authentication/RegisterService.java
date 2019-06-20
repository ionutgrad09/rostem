package rostem.service.authentication;

import static rostem.utils.ApiResponses.ACCOUNT_REGISTERED_NOT_ACTIVATED;
import static rostem.utils.ApiResponses.EMAIL_SERVICE_NOT_AVAILABLE;
import static rostem.utils.ApiResponses.INVALID_ACCOUNT_KEY;
import static rostem.utils.ApiResponses.REQUEST_OK;
import static rostem.utils.ApiResponses.USER_ALREADY_REGISTERED;

import java.util.Optional;
import java.util.UUID;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rostem.model.enums.UserType;
import rostem.model.users.RostemAdmin;
import rostem.model.users.InactiveUser;
import rostem.model.users.RostemUser;
import rostem.model.users.User;
import rostem.repository.users.RostemAdminRepository;
import rostem.repository.users.InactiveUserRepository;
import rostem.repository.users.RostemUserRepository;
import rostem.utils.exception.RostemException;

@Service
public class RegisterService {

    private final static Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private InactiveUserRepository inactiveUserRepository;
    private RostemUserRepository rostemUserRepository;
    private RostemAdminRepository adminUserRepository;
    private EmailService emailService;

    @Autowired
    RegisterService(InactiveUserRepository inactiveUserRepository, RostemAdminRepository adminUserRepository,
            RostemUserRepository rostemUserRepository, EmailService emailService) {
        this.adminUserRepository = adminUserRepository;
        this.inactiveUserRepository = inactiveUserRepository;
        this.rostemUserRepository = rostemUserRepository;
        this.emailService = emailService;
    }

    public String registerUser(RostemUser user) {
        if (inactiveUserRepository.findByEmail(user.getEmail()) != null) {
            throw new RostemException(ACCOUNT_REGISTERED_NOT_ACTIVATED);
        }
        if (rostemUserRepository.findByEmail(user.getEmail()) != null) {
            throw new RostemException(USER_ALREADY_REGISTERED);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        InactiveUser stud = InactiveUser.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .bio(user.getBio())
                .userType(UserType.USER)
                .id(UUID.randomUUID().toString())
                .build();
        try {
            emailService.sendRegisterMail(stud);
            inactiveUserRepository.save(stud);
        } catch (MessagingException ignored) {
            logger.info(EMAIL_SERVICE_NOT_AVAILABLE);
        }
        return REQUEST_OK;
    }

    public String activateUser(String id) {
        Optional<InactiveUser> inactiveUserOpt = inactiveUserRepository.findById(id);
        if (inactiveUserOpt.isPresent()) {
            inactiveUserRepository.deleteById(id);
            InactiveUser inactiveUser = inactiveUserOpt.get();
            if (inactiveUser.getUserType().equals(UserType.USER)) {
                rostemUserRepository.save(new RostemUser(inactiveUser.getEmail(), inactiveUser.getPassword(),
                        inactiveUser.getUsername(), inactiveUser.getBio()));
                return REQUEST_OK;
            }
            if (inactiveUser.getUserType().equals(UserType.ADMIN)) {
                adminUserRepository.save(new RostemAdmin(inactiveUser.getEmail(), inactiveUser.getPassword()));
                return REQUEST_OK;
            }
        }
        return INVALID_ACCOUNT_KEY;
    }
}
