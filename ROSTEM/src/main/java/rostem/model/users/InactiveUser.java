package rostem.model.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rostem.model.enums.UserType;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InactiveUser {

    @Id
    @Column(length = 120)
    private String id ;
    private String email;
    private String password;
    private String username;
    private UserType userType;

}
