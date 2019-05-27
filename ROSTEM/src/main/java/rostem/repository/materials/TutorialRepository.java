package rostem.repository.materials;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rostem.model.material.Tutorial;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

    Optional<Tutorial> findTutorialById(Long id);

    Optional<Tutorial> findTutorialByName(String name);

}
