package rostem.model.material;

import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import rostem.model.users.RostemUser;

@Entity
@Data
@Table(name = "categories")
public class Category extends Material {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Tutorial> tutorials;

    @ManyToMany(mappedBy = "favoriteCategories")
    private Set<RostemUser> users;
}
