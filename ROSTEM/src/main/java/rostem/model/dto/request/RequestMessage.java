package rostem.model.dto.request;

import java.io.Serializable;
import lombok.Data;

@Data
public class RequestMessage implements Serializable {

    private String message;
    private String from;
    private String to;

    public RequestMessage() {
    }
}
