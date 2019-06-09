package rostem.repository.materials;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rostem.model.entities.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    Chapter findChapterById(Long id);

    Chapter findChapterByName(String name);

    List<Chapter> findAllByOrderByCreationDateDesc();

}
