package rostem.model.dto.response;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class ResponseRostemUser implements Serializable {

    private String email;
    private String username;
    private String registrationDate;
    private String bio;
    private List<String> badges;

    public ResponseRostemUser(){}
}
