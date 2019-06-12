package rostem.model.dto.request;

import java.io.Serializable;
import lombok.Data;

@Data
public class RequestMessage implements Serializable {

    private String message;
    private String sentBy;
    private String receivedBy;

    public RequestMessage() {
    }
}
