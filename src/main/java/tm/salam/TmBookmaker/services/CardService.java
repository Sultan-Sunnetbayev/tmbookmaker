package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.CardDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Card;

import java.util.List;
import java.util.UUID;

public interface CardService {

    @Transactional
    ResponseTransfer<?> addCard(Card card, UUID bankUuid, UUID bettorUuid);

    ResponseTransfer<List<CardDTO>>getCardsByBettorUuid(UUID bettorUuid);

    @Transactional
    ResponseTransfer<?>editCard(Card card, UUID bankUuid);

    @Transactional
    ResponseTransfer<?>removeCardByUuid(UUID uuid);
}
