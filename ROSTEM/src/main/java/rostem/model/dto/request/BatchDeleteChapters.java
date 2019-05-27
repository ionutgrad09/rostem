package rostem.model.dto.request;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BatchDeleteChapters implements Serializable {

    public BatchDeleteChapters(){}

    List<Long> ids;
}
