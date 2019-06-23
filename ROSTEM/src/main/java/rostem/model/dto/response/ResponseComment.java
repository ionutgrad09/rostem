package rostem.model.dto.response;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class ResponseComment implements Serializable {

    private Long id;
    private String content;
    private String username;
    private String email;
    private String creationDate;
    private List<String> badges;

    public ResponseComment(){
    }
}