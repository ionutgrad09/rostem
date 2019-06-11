package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseMessage implements Serializable {

    private Long id;
    private String message;
    private String from;
    private String to;
    private String creationDate;

    public ResponseMessage() {
    }

}
