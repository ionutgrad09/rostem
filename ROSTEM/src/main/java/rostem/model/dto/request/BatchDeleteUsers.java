package rostem.model.dto.request;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BatchDeleteUsers implements Serializable {

    public BatchDeleteUsers(){}

    List<String> emails;
}
