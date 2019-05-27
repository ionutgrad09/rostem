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

    public String registerUser(User user) {
        if (inactiveUserRepository.findByEmail(user.getEmail()) != null) {
            return ACCOUNT_REGISTERED_NOT_ACTIVATED;
        }
        if (rostemUserRepository.findByEmail(user.getEmail()) != null) {
            return USER_ALREADY_REGISTERED;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        InactiveUser stud = InactiveUser.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
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
                        inactiveUser.getUsername()));
                return REQUEST_OK;
            }
            if (inactiveUser.getUserType().equals(UserType.ADMIN)) {
                adminUserRepository.save(new RostemAdmin(inactiveUser.getEmail(), inactiveUser.getPassword(),
                        inactiveUser.getUsername()));
                return REQUEST_OK;
            }
        }
        return INVALID_ACCOUNT_KEY;
    }

    public String acceptUser(String id, String password) {
        Optional<InactiveUser> inactiveUserOpt = inactiveUserRepository.findById(id);
        if (inactiveUserOpt.isPresent()) {
            inactiveUserRepository.deleteById(id);
            InactiveUser user = inactiveUserOpt.get();
            rostemUserRepository.save(new RostemUser(user.getEmail(), encoder.encode(password), user.getUsername()));
            return REQUEST_OK;
        }
        return INVALID_ACCOUNT_KEY;
    }

    public String inviteFriend(String email, String name) {
        if (inactiveUserRepository.findByEmail(email) != null) {
            return ACCOUNT_REGISTERED_NOT_ACTIVATED;
        }
        if (rostemUserRepository.findByEmail(email) != null) {
            return USER_ALREADY_REGISTERED;
        }
        InactiveUser newUser = InactiveUser.builder()
                .email(email)
                .username(name)
                .userType(UserType.USER)
                .id(UUID.randomUUID().toString())
                .build();
        try {
            emailService.sendInviteMail(newUser);
        } catch (MessagingException e) {
            return EMAIL_SERVICE_NOT_AVAILABLE;
        }
        inactiveUserRepository.save(newUser);
        return REQUEST_OK;
    }

}
