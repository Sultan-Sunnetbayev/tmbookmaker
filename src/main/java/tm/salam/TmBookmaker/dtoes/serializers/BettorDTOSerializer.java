package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.BettorDTO;
import tm.salam.TmBookmaker.models.Bettor;

@Component
public class BettorDTOSerializer {

    public BettorDTO toBettorDTOGeneral(final Bettor bettor){

        if(bettor==null){

            return null;
        }

        return BettorDTO.builder()
                .uuid(bettor.getUuid())
                .username(bettor.getUsername())
                .phoneNumber(bettor.getPhoneNumber())
                .deposit(bettor.getDeposit())
                .winnings(bettor.getWinnings())
                .cashOut(bettor.getCashOut())
                .build();
    }

    public BettorDTO toBettorDTOProfileForAppClient(final Bettor bettor){

        if(bettor==null){

            return null;
        }

        return BettorDTO.builder()
                .uuid(bettor.getUuid())
                .username(bettor.getUsername())
                .phoneNumber(bettor.getPhoneNumber())
                .deposit(bettor.getDeposit())
                .winnings(bettor.getWinnings())
                .cashOut(bettor.getCashOut())
                .isRegistered(bettor.isRegistered())
                .build();
    }

}
