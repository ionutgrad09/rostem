package rostem.model.dto.request;

import java.io.Serializable;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestTutorial implements Serializable {

    @NotNull(message = "Category's id can't be null.")
    private Long categoryId;

    @NotBlank(message = "Tutorial's name can't be blank.")
    private String name;

    private String description;

    public RequestTutorial() {
    }
}
