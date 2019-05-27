package rostem.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import rostem.model.users.RostemUser;

@Data
@NoArgsConstructor
public class RostemUserDto extends SimpleUserDto {

    public RostemUserDto(RostemUser rostemUser) {super(rostemUser);}
}
