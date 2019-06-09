package rostem.repository.materials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rostem.model.entities.Tutorial;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

    Tutorial findTutorialById(Long id);

    Tutorial findTutorialByName(String name);

}
