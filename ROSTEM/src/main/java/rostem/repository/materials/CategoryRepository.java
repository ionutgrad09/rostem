package rostem.repository.materials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rostem.model.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findCategoryById(Long id);

    Category findCategoryByName(String name);

}
