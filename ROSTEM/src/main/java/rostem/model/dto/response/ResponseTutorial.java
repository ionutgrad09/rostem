package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseTutorial implements Serializable {

    private Long id;
    private String categoryName;
    private String name;
    private String description;

    public ResponseTutorial(){

    }
}
