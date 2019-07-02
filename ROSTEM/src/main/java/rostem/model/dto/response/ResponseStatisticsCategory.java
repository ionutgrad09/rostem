package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseStatisticsCategory implements Serializable {

    private float percentage;
    private int noTutorials;
    private String name;
    private int noFavorites;

    public ResponseStatisticsCategory(float percentage, int noTutorials, String name, int noFavorites) {
        this.percentage = percentage;
        this.noTutorials = noTutorials;
        this.name = name;
        this.noFavorites = noFavorites;
    }
}
