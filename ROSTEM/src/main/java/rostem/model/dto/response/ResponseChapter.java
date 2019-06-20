package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseChapter implements Serializable {

    private Long id;
    private String categoryName;
    private String tutorialName;
    private String name;
    private String content;
    private String sourceUrl;
    private String creationDate;
    private boolean isDone;
    private boolean isTodo;
    private boolean isLiked;

    public ResponseChapter(){

    }
}
