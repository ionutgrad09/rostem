package rostem.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import rostem.model.users.InactiveUser;

public interface InactiveUserRepository extends JpaRepository<InactiveUser, String> {

    InactiveUser findByEmail(String email);

}
