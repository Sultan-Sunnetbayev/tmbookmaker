package tm.salam.TmBookmaker.helpers;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTransfer<T> {

    private HttpStatusCode httpStatus;
    private ResponseBody<T>responseBody;

}
