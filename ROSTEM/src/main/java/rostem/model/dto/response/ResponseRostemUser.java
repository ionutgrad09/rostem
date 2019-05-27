package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseRostemUser implements Serializable {

    private String email;
    private String username;
    private String registrationDate;

    public ResponseRostemUser(){}
}
