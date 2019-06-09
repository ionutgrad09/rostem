package rostem.repository.materials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rostem.model.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findCommentById(Long id);
}
