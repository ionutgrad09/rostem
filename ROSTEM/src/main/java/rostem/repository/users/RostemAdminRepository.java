package rostem.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import rostem.model.users.RostemAdmin;

public interface RostemAdminRepository extends JpaRepository<RostemAdmin, String> {

    RostemAdmin findByEmail(String email);
}
