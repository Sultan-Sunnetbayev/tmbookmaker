package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Bet;
import tm.salam.TmBookmaker.models.Horse;

import java.util.UUID;

public interface BetService {

    @Transactional
    ResponseTransfer<?> addBet(Bet bet, Horse[] chosenHorses, UUID betOptionUuid, UUID horseRaceUuid, UUID bettorUuid);

}
