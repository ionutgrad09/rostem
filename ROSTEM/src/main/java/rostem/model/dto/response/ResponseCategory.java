package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseCategory implements Serializable {

    private Long id;
    private String name;
    private String description;
    private boolean isMarkedAsFavorite;

    public ResponseCategory() {

    }
}
