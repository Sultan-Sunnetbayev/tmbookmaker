package tm.salam.TmBookmaker.forms;

import lombok.*;
import tm.salam.TmBookmaker.dtoes.models.BetOptionDTO;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceDTO;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormCreateBetStep3 {

    private HorseRaceEventDTO horseRaceEventDTO;
    private HorseRaceDTO horseRaceDTO;
    private BetOptionDTO betOptionDTO;
}
