package rostem.model.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestComment implements Serializable {

    @NotNull(message = "Category's id can't be null.")
    private Long chapterId;

    private String content;

    private String username;

    public RequestComment() {
    }
}