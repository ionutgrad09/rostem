package rostem.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rostem.model.users.RostemAdmin;
import rostem.model.users.RostemUser;
import rostem.model.users.UserLoginDetail;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDetailDto implements Serializable {
    private SimpleUserDto user;
    private String role;

    public UserLoginDetailDto(UserLoginDetail userLoginDetail) {
        if (userLoginDetail.getUser() instanceof RostemUser){
            user = new RostemUserDto((RostemUser) userLoginDetail.getUser());
        } else if (userLoginDetail.getUser() instanceof RostemAdmin)  {
            user = new RostemAdminDto((RostemAdmin) userLoginDetail.getUser());
        }
        role = userLoginDetail.getRole();
    }
}
