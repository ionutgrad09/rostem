package rostem.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rostem.model.users.RostemAdmin;

@Data
@NoArgsConstructor
public class RostemAdminDto extends SimpleUserDto {

    RostemAdminDto(RostemAdmin rostemAdmin) {
        super(rostemAdmin);
    }
}
