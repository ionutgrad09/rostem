package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseMessage implements Serializable {

    private Long id;
    private String message;
    private String sentBy;
    private String receivedBy;
    private String creationDate;

    public ResponseMessage() {
    }

}
