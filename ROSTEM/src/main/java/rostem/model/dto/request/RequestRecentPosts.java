package rostem.model.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestRecentPosts implements Serializable {

    private int counter = 5;

    @NotBlank(message = "Email can't be blank.")
    private String email;

    public RequestRecentPosts() {
    }
}
