package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.BetDTO;
import tm.salam.TmBookmaker.models.Bet;

@Component
public class BetDTOSerializer {

    private final BetOptionDTOSerializer betOptionDTOSerializer;
    private final MoneyDTOSerializer moneyDTOSerializer;


    public BetDTOSerializer(BetOptionDTOSerializer betOptionDTOSerializer, MoneyDTOSerializer moneyDTOSerializer) {
        this.betOptionDTOSerializer = betOptionDTOSerializer;
        this.moneyDTOSerializer = moneyDTOSerializer;
    }

    public BetDTO toBetDTO(final Bet bet){

        if(bet==null){
            return null;
        }

        return BetDTO.builder()
                .uuid(bet.getUuid())
                .betOptionDTO(betOptionDTOSerializer.toBetOptionDTOOnlyName(bet.getBetOption()))
                .transactedMoney(moneyDTOSerializer.toMoneyDTO(bet.getTransactedMoney()))
                .status(bet.getStatus())
                .build();
    }

    public BetDTO toBetDTOOnlyTransactedMoney(final Bet bet){

        if(bet==null){

            return null;
        }

        return BetDTO.builder()
                .status(bet.getStatus())
                .transactedMoney(moneyDTOSerializer.toMoneyDTO(bet.getTransactedMoney()))
                .build();
    }
}
