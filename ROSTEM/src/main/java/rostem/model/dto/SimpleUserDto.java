package rostem.model.dto;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import rostem.model.users.User;

@Data
@NoArgsConstructor
class SimpleUserDto implements Serializable {

    private String email;

    public SimpleUserDto(User u) {
        email = u.getEmail();
    }
}
