package rostem.model.users;


import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Table(name = "ROSTEM_ADMINS")
public class RostemAdmin extends User {

    public RostemAdmin(String email, String password, String username) {
        super(email, password, username);
    }
}
