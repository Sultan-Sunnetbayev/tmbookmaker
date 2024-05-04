package tm.salam.TmBookmaker.services;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.dtoes.models.CityDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.City;

import java.util.List;
import java.util.UUID;

public interface CityService {

    @Transactional
    ResponseTransfer<?> addCity(City city, UUID regionUuid);

    ResponseTransfer<Integer> getNumberCitiesBySearchKey(String searchKey);

    ResponseTransfer<List<CityDTO>> getCityDTOSBySearchKey(String searchKey, int page, int size, SortOption[] sortOptions);

    ResponseTransfer<CityDTO> getCityDTOByUuid(UUID uuid);

    @Transactional
    ResponseTransfer<?> editCity(City city, UUID regionUuid);

    @Transactional
    ResponseTransfer<?> removeCityByUuid(UUID uuid);

    ResponseTransfer<List<CityDTO>> getActiveCityDTOSByRegionUuid(UUID regionUuid);

    ResponseTransfer<List<CityDTO>> getActiveCityDTOS();

    @Transactional
    ResponseTransfer<?>switchActivationCityByUuid(UUID uuid, boolean isActive);

}
