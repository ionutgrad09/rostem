package rostem.model.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestCategory implements Serializable {

    @NotBlank(message = "The category name can't be blank")
    private String name;

    @Size(max = 255, message = "Description max size must be 255.")
    private String description;

    public RequestCategory() {

    }
}
