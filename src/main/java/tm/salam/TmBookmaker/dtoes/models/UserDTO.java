package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID uuid;
    private String name;
    private String surname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String login;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RoleDTO roleDTO;

}
