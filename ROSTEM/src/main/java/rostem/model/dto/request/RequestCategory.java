package rostem.model.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestCategory implements Serializable {

    @NotBlank(message = "The category name can't be blank")
    private String name;

    private String description;

    public RequestCategory() {

    }
}
