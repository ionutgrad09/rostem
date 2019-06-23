package rostem.model.dto.request;

import java.io.Serializable;
import lombok.Data;

@Data
public class RequestRostemUser implements Serializable {

    private String email;
    private String newUsername;
    private String newBio;

    public RequestRostemUser() {
    }
}
