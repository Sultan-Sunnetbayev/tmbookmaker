package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Horse;
import tm.salam.TmBookmaker.models.HorseRace;

import java.util.List;
import java.util.UUID;

public interface HorseRaceService {

    @Transactional
    ResponseTransfer<?> addHorseRace(HorseRace horseRace, UUID dataFileUuid, UUID[] betOptionUuids, UUID horseRaceEventUuid);

    ResponseTransfer<Integer>getNumberHorseRacesBySearchKey(String searchKey, boolean isActive,
                                                           UUID horseRaceEventUuid);

    ResponseTransfer<List<HorseRaceDTO>>getHorseRaceDTOSBySearchKey(int page, int size, String searchKey,
                                                                    boolean isActive, SortOption[] sortOptions,
                                                                    UUID horseRaceEventUuid);

    ResponseTransfer<HorseRaceDTO>getHorseRaceDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>switchActivationHorseRaceByUuid(UUID uuid, boolean isActive);

    @Transactional
    ResponseTransfer<?>switchHorseRaceBetPermission(UUID horseRaceUuid, boolean betPermission);

    ResponseTransfer<List<HorseRaceDTO>>getHorseRaceDTOSByHorseRaceEventUuidForBettorBets(UUID horseRaceEventUuid,
                                                                                          UUID bettorUuid);

    ResponseTransfer<?>isHorseRaceClosed(UUID horseRaceUuid);

    @Transactional
    ResponseTransfer<?>addResultHorseRace(UUID horseRaceUuid, Horse[] horses);

    ResponseTransfer<List<HorseRaceDTO>>getResultHorseRaceDTOSByHorseRaceEventUuidAndBettorUuid(UUID horseRaceEventUuid,
                                                                                                UUID bettorUuid);

    ResponseTransfer<HorseRaceDTO>getHorseRaceDTOByUuidForCreateBet(UUID horseRaceUuid);

}
