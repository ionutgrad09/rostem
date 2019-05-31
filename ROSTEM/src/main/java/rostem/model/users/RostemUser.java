package rostem.model.users;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import rostem.model.material.Category;

@Entity
@NoArgsConstructor
@Data
@Table(name = "ROSTEM_USERS")
public class RostemUser extends User {

    @Column(name = "REGISTRATION_DATE")
    protected Date registrationDate;

    @ManyToMany
    @JoinTable(
            name = "USER_CATEGORIES",
            joinColumns = @JoinColumn(name = "EMAIL"),
            inverseJoinColumns = @JoinColumn(name = "ID")
    )
    private Set<Category> favoriteCategories;

    public RostemUser(String email, String password, String username) {

        super(email, password, username);
        this.registrationDate = getCurrentDate();
    }

    private Date getCurrentDate() {
        return new Date();
    }
}
