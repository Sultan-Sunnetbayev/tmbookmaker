package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.HorseRaceEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface HorseRaceEventService {

    @Transactional
    ResponseTransfer<?> addHorseRaceEvent(HorseRaceEvent horseRaceEvent, UUID racetrackUuid);

    ResponseTransfer<Integer>getNumberHorseRaceEventsBySearchKey(String searchKey, boolean isActive);

    ResponseTransfer<List<HorseRaceEventDTO>> getHorseRaceEventDTOSBySearchKey(int page, int size,
                                                                               String searchKey, SortOption[] sortOptions,
                                                                               boolean isActive);

    ResponseTransfer<HorseRaceEventDTO> getHorseRaceEventDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>editHorseRaceEvent(HorseRaceEvent horseRaceEvent, UUID racetrackUuid);

    @Transactional
    ResponseTransfer<?>removeHorseRaceEventByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>switchActivationHorseRaceEventByUuid(UUID uuid, boolean isActive);

    ResponseTransfer<List<HorseRaceEventDTO>>getActiveHorseRaceEventDTOS();

    ResponseTransfer<List<HorseRaceEventDTO>>getLastHorseRaceEventDTOS();

    ResponseTransfer<List<HorseRaceEventDTO>>getHorseRaceEventDTOSByDate(LocalDate date);
}
