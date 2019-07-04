package rostem.model.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import rostem.model.users.RostemUser;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "CATEGORIES")
public class Category extends Material {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Tutorial> tutorials;

    @ManyToMany(mappedBy = "favoriteCategories")
    private List<RostemUser> users;

    @Override
    public String toString() {
        return "Category{" +
                "tutorials=" + tutorials +
                ", users=" + users +
                '}';
    }
}
