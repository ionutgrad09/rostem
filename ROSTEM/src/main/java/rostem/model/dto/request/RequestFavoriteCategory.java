package rostem.model.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestFavoriteCategory implements Serializable {

    @NotBlank(message = "The category name can't be blank")
    private String email;

    private Long id;

    public RequestFavoriteCategory() {

    }
}
