package rostem.model.dto.request;

import java.io.Serializable;
import lombok.Data;

@Data
public class RequestActionChapter implements Serializable {

    private Long tutorialId;

    private Long chapterId;

    private String email;

    private String actionType;

    public RequestActionChapter() {
    }
}
