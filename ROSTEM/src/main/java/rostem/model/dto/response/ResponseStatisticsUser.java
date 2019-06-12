package rostem.model.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResponseStatisticsUser implements Serializable {

    private int noComments;
    private int todoChapters;
    private int doneChapters;
    private int likedChapters;
    private int favoriteCategories;
    private String email;

    public ResponseStatisticsUser(int noComments, int todoChapters, int doneChapters, int likedChapters,
            int favoriteCategories, String email) {
        this.noComments = noComments;
        this.todoChapters = todoChapters;
        this.doneChapters = doneChapters;
        this.likedChapters = likedChapters;
        this.favoriteCategories = favoriteCategories;
        this.email = email;
    }

}
