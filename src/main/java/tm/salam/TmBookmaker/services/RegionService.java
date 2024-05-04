package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.RegionDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Region;

import java.util.List;
import java.util.UUID;

public interface RegionService {

    @Transactional
    ResponseTransfer<?> addRegion(Region region);

    ResponseTransfer<Integer> getNumberRegionsBySearchKey(String searchKey);

    ResponseTransfer<List<RegionDTO>> getRegionDTOSBySearchKey(String searchKey, int page, int size,
                                                               SortOption[] sortOptions);

    ResponseTransfer<RegionDTO> getRegionDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?> editRegion(Region region);

    @Transactional
    ResponseTransfer<?> removeRegionByUuid(UUID uuid);

    ResponseTransfer<List<RegionDTO>> getActiveRegionDTOS();

    @Transactional
    ResponseTransfer<?>switchActivationRegionByUuid(UUID uuid, boolean isActive);

}
