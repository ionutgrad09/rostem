package rostem.model.users;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rostem.model.entities.Category;
import rostem.model.entities.Chapter;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
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
    private List<Category> favoriteCategories;

    @ManyToMany
    @JoinTable(
            name = "USER_TODO",
            joinColumns = @JoinColumn(name = "EMAIL"),
            inverseJoinColumns = @JoinColumn(name = "ID")
    )
    private List<Chapter> todoChapters;

    @ManyToMany
    @JoinTable(
            name = "USER_DONE",
            joinColumns = @JoinColumn(name = "EMAIL"),
            inverseJoinColumns = @JoinColumn(name = "ID")
    )
    private List<Chapter> doneChapters;

    public RostemUser(String email, String password, String username) {

        super(email, password, username);
        this.registrationDate = getCurrentDate();
    }

    private Date getCurrentDate() {
        return new Date();
    }
}
