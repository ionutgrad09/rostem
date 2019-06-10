package rostem.model.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "INACTIVE_USERS")
public class InactiveUser {

    @Id
    @Column(name = "ID", length = 120)
    private String id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "USER_TYPE")
    private UserType userType;

    @Column(name = "BIO")
    private String bio;

}
