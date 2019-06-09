package rostem.utils.mapper;

import java.util.Date;
import rostem.model.dto.request.RequestComment;
import rostem.model.dto.response.ResponseComment;
import rostem.model.entities.Comment;

public class CommentsMapper {

    public static Comment map(RequestComment requestComment) {
        Comment comment = new Comment();
        comment.setContent(requestComment.getContent());
        comment.setUsername(requestComment.getUsername());
        comment.setCreationDate(new Date());
        return comment;
    }

    public static ResponseComment map(Comment comment) {
        ResponseComment requestComment = new ResponseComment();
        requestComment.setId(comment.getId());
        requestComment.setContent(comment.getContent());
        requestComment.setCreationDate(comment.getCreationDate().toString());
        requestComment.setUsername(comment.getUsername());
        return requestComment;
    }
}
