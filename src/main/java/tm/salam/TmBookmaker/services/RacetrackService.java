package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.RacetrackDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Racetrack;

import java.util.List;
import java.util.UUID;


public interface RacetrackService {

    @Transactional
    ResponseTransfer<?> addRacetrack(Racetrack racetrack, UUID cityUuid, UUID regionUuid);

    ResponseTransfer<Integer>getNumberRacetracksBySearchKey(String searchKey);

    ResponseTransfer<List<RacetrackDTO>> getRacetrackDTOSBySearchKey(int page, int size, String searchKey,
                                                                     SortOption[] sortOptions);

    ResponseTransfer<RacetrackDTO> getRacetrackDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>editRacetrack(Racetrack racetrack, UUID cityUuid, UUID regionUuid);

    @Transactional
    ResponseTransfer<?>removeRacetrackByUuid(UUID uuid);

    ResponseTransfer<List<RacetrackDTO>>getActiveRacetrackDTOS();

    @Transactional
    ResponseTransfer<?>switchActivationRacetrackByUuid(UUID uuid, boolean isActive);

}
