package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.PopulatedPlaceDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.PopulatedPlace;

import java.util.List;
import java.util.UUID;

public interface PopulatedPlaceService {
    @Transactional
    ResponseTransfer<?> addPopulatedPlace(PopulatedPlace populatedPlace, UUID cityUuid,
                                          UUID regionUuid);

    ResponseTransfer<Integer>getNumberPopulatedPlacesBySearchKey(String searchKey);

    ResponseTransfer<List<PopulatedPlaceDTO>>getPopulatedPlaceDTOSBySearchKey(int page, int size, String searchKey,
                                                                              SortOption[] sortOptions);

    ResponseTransfer<PopulatedPlaceDTO>getPopulatedPlaceDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>editPopulatedPlace(PopulatedPlace populatedPlace, UUID cityUuid,
                                          UUID regionUuid);

    @Transactional
    ResponseTransfer<?>removePopulatedPlaceByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?>switchActivationPopulatedPlaceByUuid(UUID uuid, boolean isActive);

    ResponseTransfer<List<PopulatedPlaceDTO>>getActivePopulatedPlaceDTOS(UUID cityUuid, UUID regionUuid);
}
