package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseTutorialProgress implements Serializable {

    private String name;
    private int doneChapters;
    private int totalChapters;
    private float percentage;

    public ResponseTutorialProgress(){

    }
}
