package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID uuid;
    private String number;
    private Boolean isActive;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer place;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BetHorseDTO betHorseDTO;

}
