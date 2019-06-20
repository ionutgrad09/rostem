package rostem.model.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import rostem.model.users.RostemUser;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "CHAPTERS")
public class Chapter extends Material {

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTORIAL_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tutorial tutorial;

    @ManyToMany(mappedBy = "todoChapters")
    private List<RostemUser> todoUserList;

    @ManyToMany(mappedBy = "doneChapters")
    private List<RostemUser> doneUserList;

    @ManyToMany(mappedBy = "likedChapters")
    private List<RostemUser> userLikes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chapter")
    @OrderBy(value = "creationDate asc")
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Chapter{" +
                ", content=" + content +
                ", creationDate=" + creationDate +
                ", tutorial=" + tutorial +
                ", todoUserList=" + todoUserList +
                ", doneUserList=" + doneUserList +
                ", userLikes=" + userLikes +
                ", comments=" + comments +
                '}';
    }
}
