package rostem.model.dto.request;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BatchDeleteTutorials implements Serializable {

    public BatchDeleteTutorials(){}

    List<Long> ids;
}
