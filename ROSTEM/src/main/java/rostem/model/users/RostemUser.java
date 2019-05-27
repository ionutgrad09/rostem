package rostem.model.users;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import rostem.model.material.Category;

@Entity
@NoArgsConstructor
@Data
public class RostemUser extends User {

    protected String registrationDate;

    @ManyToMany
    @JoinTable(
            name = "user_categories",
            joinColumns = @JoinColumn(name = "email"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Set<Category> favoriteCategories;

    public RostemUser(String email, String password, String username) {

        super(email, password, username);
        this.registrationDate = getCurrentDate();
    }

    private String getCurrentDate(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }
}
