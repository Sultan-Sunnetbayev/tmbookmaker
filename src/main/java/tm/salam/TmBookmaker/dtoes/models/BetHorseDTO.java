package tm.salam.TmBookmaker.dtoes.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetHorseDTO {

    private int place;
    private Boolean isCorrect;
}
