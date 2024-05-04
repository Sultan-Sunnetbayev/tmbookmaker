package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.BetHorseDTO;
import tm.salam.TmBookmaker.models.BetHorse;

@Component
public class BetHorseDTOSerializer {

    public BetHorseDTO toBetHorseDTOOnlyPlaceAndState(BetHorse betHorse){

        if(betHorse==null){

            return null;
        }

        return BetHorseDTO.builder()
                .place(betHorse.getPlace())
                .isCorrect(betHorse.getIsCorrect())
                .build();
    }

}
