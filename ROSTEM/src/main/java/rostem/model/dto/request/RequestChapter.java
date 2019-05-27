package rostem.model.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestChapter implements Serializable {

    @NotNull(message = "Tutorial's id can't be null.")
    private Long tutorialId;

    @NotBlank(message = "Chapter's name can't be blank.")
    private String name;

    @Size(max = 255, message = "Description max size must be 255.")
    private String description;

    public RequestChapter() {
    }
}
