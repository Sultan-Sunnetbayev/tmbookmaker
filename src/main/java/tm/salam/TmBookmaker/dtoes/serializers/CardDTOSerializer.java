package tm.salam.TmBookmaker.dtoes.serializers;

import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.dtoes.models.CardDTO;
import tm.salam.TmBookmaker.models.Card;

@Component
public class CardDTOSerializer {

    private final BankDTOSerializer bankDTOSerializer;

    public CardDTOSerializer(BankDTOSerializer bankDTOSerializer) {
        this.bankDTOSerializer = bankDTOSerializer;
    }

    public CardDTO toCardDTO(final Card card){

        if(card==null){
            return null;
        }

        return CardDTO.builder()
                .uuid(card.getUuid())
                .cardNumber(card.getCardNumber())
                .holderName(card.getHolderName())
                .cvcCode(card.getCvcCode())
                .expirationDate(card.getExpirationDate())
                .bankDTO(bankDTOSerializer.toBankDTO(card.getBank()))
                .build();
    }

}
