package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashRegisterDTO {

    private UUID uuid;
    private String number;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDTO cashierDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PopulatedPlaceDTO populatedPlaceDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CityDTO cityDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RegionDTO regionDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isActive;

}
