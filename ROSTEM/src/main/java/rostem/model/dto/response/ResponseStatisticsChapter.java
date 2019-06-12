package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseStatisticsChapter implements Serializable {

    private int noLikes;
    private int noTodo;
    private int noDone;
    private int noComments;
    private String name;

    public ResponseStatisticsChapter(int noLikes, int noTodo, int noDone, int noComments, String name) {
        this.noLikes = noLikes;
        this.noDone = noDone;
        this.noTodo = noTodo;
        this.noComments = noComments;
        this.name = name;
    }
}
