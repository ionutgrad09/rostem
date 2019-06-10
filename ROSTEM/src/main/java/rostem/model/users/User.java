package rostem.model.users;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    @Id
    @Column(name = "EMAIL", length = 128)
    protected String email;

    @Column(name = "PASSWORD")
    protected String password;

    public User(String email){
        this.email = email;
    }
}

