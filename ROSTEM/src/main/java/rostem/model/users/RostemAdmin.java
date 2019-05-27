package rostem.model.users;


import javax.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RostemAdmin extends User {

    public RostemAdmin(String email, String password, String username) {
        super(email, password, username);
    }
}
