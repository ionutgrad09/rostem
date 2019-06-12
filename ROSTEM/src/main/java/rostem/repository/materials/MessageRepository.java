package rostem.repository.materials;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rostem.model.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllBySentByOrderByCreationDateDesc(String from);

    List<Message> findAllByReceivedByOrderByCreationDateDesc(String from);
}
