package tm.salam.TmBookmaker.dtoes.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopulatedPlaceDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID uuid;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CityDTO cityDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RegionDTO regionDTO;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isActive;

}
