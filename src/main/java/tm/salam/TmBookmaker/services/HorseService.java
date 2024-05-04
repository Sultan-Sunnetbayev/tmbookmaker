package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.HorseDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Horse;

import java.util.List;
import java.util.UUID;

public interface HorseService {
    @Transactional
    ResponseTransfer<?> addHorses(List<Horse> horses, UUID horseRaceUuid);

    @Transactional
    ResponseTransfer<?>addHorseRaceResult(Horse[] horses);

    @Transactional
    ResponseTransfer<?>switchActivationHorses(Horse[] horses);

    ResponseTransfer<List<HorseDTO>>getHorsesByHorseRaceUuidForCreateBet(UUID horseRaceUuid);
}
