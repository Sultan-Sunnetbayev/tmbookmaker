package tm.salam.TmBookmaker.forms;

import lombok.*;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceDTO;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormCreateBetStep2 {

    private HorseRaceEventDTO horseRaceEventDTO;
    private List<HorseRaceDTO> horseRaceDTOS;

}
