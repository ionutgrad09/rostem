package rostem.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import rostem.model.users.RostemUser;

public interface RostemUserRepository extends JpaRepository<RostemUser, String> {

    RostemUser findByEmail(String email);

    void deleteByEmail(String email);
}
