package rostem.model.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestFavoriteCategory implements Serializable {

    private String email;
    private String categoryName;
    private Long id;

    public RequestFavoriteCategory() {

    }
}
