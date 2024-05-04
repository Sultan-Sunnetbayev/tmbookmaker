package tm.salam.TmBookmaker.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBody<T> {

    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

}
