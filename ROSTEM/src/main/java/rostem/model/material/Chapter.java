package rostem.model.material;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import rostem.model.users.RostemUser;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CHAPTERS")
public class Chapter extends Material {

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "SOURCE_URL")
    private String sourceUrl;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTORIAL_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tutorial tutorial;

    @ManyToMany(mappedBy = "todoChapters")
    private Set<RostemUser> todoUserList;

    @ManyToMany(mappedBy = "doneChapters")
    private Set<RostemUser> doneUserList;
}
