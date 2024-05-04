package tm.salam.TmBookmaker.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.CardRepository;
import tm.salam.TmBookmaker.dtoes.models.CardDTO;
import tm.salam.TmBookmaker.dtoes.serializers.CardDTOSerializer;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Card;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardDTOSerializer cardDTOSerializer;

    public CardServiceImpl(CardRepository cardRepository, CardDTOSerializer cardDTOSerializer) {
        this.cardRepository = cardRepository;
        this.cardDTOSerializer = cardDTOSerializer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>addCard(final Card card, final UUID bankUuid, final UUID bettorUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isAdded=cardRepository.addCard(card.getCardNumber(), card.getHolderName(), card.getCvcCode(),
                card.getExpirationDate(), bankUuid, bettorUuid);

        if(Boolean.TRUE.equals(isAdded)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept card successful added")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error card don't added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<CardDTO>>getCardsByBettorUuid(final UUID bettorUuid) {

        final ResponseTransfer<List<CardDTO>> responseTransfer;
        final List<Card> cards = cardRepository.getCardsByBettorUuid(bettorUuid);
        final List<CardDTO> cardDTOS = new LinkedList<>();

        if (cards != null) {
            for (Card card : cards) {
                cardDTOS.add(cardDTOSerializer.toCardDTO(card));
            }
        }
        responseTransfer = ResponseTransfer.<List<CardDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<CardDTO>>builder()
                        .message("accept all cards bettor's successful returned")
                        .data(cardDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>editCard(final Card card, final UUID bankUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isEdited=cardRepository.editCard(card.getUuid(), card.getCardNumber(), card.getHolderName(),
                card.getCvcCode(), card.getExpirationDate(), bankUuid);

        if (Boolean.TRUE.equals(isEdited)) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept card successful edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error card don't edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>removeCardByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isRemoved=cardRepository.removeCardByUuid(uuid);

        if(Boolean.TRUE.equals(isRemoved)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept card successful removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error card don't removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
