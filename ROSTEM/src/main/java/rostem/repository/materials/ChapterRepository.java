package rostem.repository.materials;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rostem.model.material.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    Optional<Chapter> findChapterById(Long id);

    Optional<Chapter> findChapterByName(String name);

}
