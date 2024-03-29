package rostem.utils.mapper;

import rostem.model.dto.response.ResponseRostemUser;
import rostem.model.users.RostemUser;

public class RostemUserMapper {

    public static ResponseRostemUser map(RostemUser rostemUser) {
        ResponseRostemUser responseRostemUser = new ResponseRostemUser();
        responseRostemUser.setEmail(rostemUser.getEmail());
        responseRostemUser.setUsername(rostemUser.getUsername());
        responseRostemUser.setRegistrationDate(rostemUser.getRegistrationDate().toString());

        responseRostemUser.setBio(rostemUser.getBio() != null ? rostemUser.getBio() : "");
        // responseRostemUser.setPhoto(rostemUser.getPhoto() != null ? rostemUser.getPhoto() : "".getBytes());
        return responseRostemUser;
    }
}
