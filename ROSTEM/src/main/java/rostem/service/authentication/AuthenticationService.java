package rostem.service.authentication;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rostem.model.users.User;
import rostem.model.users.UserPrincipal;
import rostem.repository.users.RostemAdminRepository;
import rostem.repository.users.RostemUserRepository;


@Service
public class AuthenticationService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final RostemUserRepository rostemUserRepository;

    private final RostemAdminRepository rostemAdminRepository;

    @Autowired
    public AuthenticationService(RostemUserRepository rostemUserRepository,
            RostemAdminRepository rostemAdminRepository) {
        this.rostemUserRepository = rostemUserRepository;
        this.rostemAdminRepository = rostemAdminRepository;
    }

    //  Searches for a user with a given email in all 2 databases (Student and Professor)
    //  In our case the username is actually the email
    //  If there is no user found with the given id -> UsernameNotFoundException
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.info("[loadUserByUsername] Searching for user " + email + " in databases");
        Optional<User> user = Optional.ofNullable(rostemUserRepository.findByEmail(email));
        if (!user.isPresent()) {
            user = Optional.ofNullable(rostemAdminRepository.findByEmail(email));
            if (!user.isPresent()) {

                logger.error("[loadUserByUsername] Error finding user");
                throw new UsernameNotFoundException(email);
            }
        }
        logger.info("[loadUserByUsername] Found user, all ok");
        return new UserPrincipal(user.get());
    }
}
