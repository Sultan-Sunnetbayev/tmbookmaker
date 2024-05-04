package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.CityRepository;
import tm.salam.TmBookmaker.dtoes.serializers.CityDTOSerializer;
import tm.salam.TmBookmaker.dtoes.models.CityDTO;
import tm.salam.TmBookmaker.helpers.*;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.City;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityDTOSerializer cityDTOSerializer;
    private final PaginationBuilder paginationBuilder;

    public CityServiceImpl(CityRepository cityRepository, CityDTOSerializer cityDTOSerializer,
                           PaginationBuilder paginationBuilder) {
        this.cityRepository = cityRepository;
        this.cityDTOSerializer = cityDTOSerializer;
        this.paginationBuilder = paginationBuilder;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addCity(final City city, final UUID regionUuid){

        final ResponseTransfer<?> responseTransfer;
        final Boolean isAdded=cityRepository.addCity(city.getName(), regionUuid);

        if(isAdded==null || !isAdded){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error city don't added")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept city successful added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer> getNumberCitiesBySearchKey(String searchKey){

        final ResponseTransfer<Integer>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberCities= cityRepository.getNumberCitiesBySearchKey(searchKey);

        responseTransfer=ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("number cities by search key")
                        .data(numberCities)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<CityDTO>> getCityDTOSBySearchKey(String searchKey, final int page, final int size,
                                                                  SortOption[] sortOptions){

        final ResponseTransfer<List<CityDTO>> responseTransfer;
        sortOptions =parseCityColumns(sortOptions);

        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        List<City>cities=cityRepository.getCitiesBySearchKey(pageable, searchKey);
        List<CityDTO>cityDTOS=new LinkedList<>();

        if(cities!=null){
            for(City city:cities){
                cityDTOS.add(cityDTOSerializer.toCityDTOGeneral(city));
            }
        }

        responseTransfer= ResponseTransfer.<List<CityDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<CityDTO>>builder()
                        .message("accept all founded cities successful returned")
                        .data(cityDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parseCityColumns(final SortOption[] sortOptions) {

        if (sortOptions == null) {
            return null;
        }
        List<SortOption>parsedSortOptions=new LinkedList<>();

        for(SortOption sortOption:sortOptions){
            switch(sortOption.getField()){
                case "name" -> {
                    parsedSortOptions.add(sortOption);
                }
                case "regionName" -> {
                    sortOption.setField("region.name");
                    parsedSortOptions.add(sortOption);
                }
            }
        }

        return parsedSortOptions.toArray(SortOption[] ::new);
    }

    @Override
    public ResponseTransfer<CityDTO> getCityDTOByUuid(final UUID uuid){

        ResponseTransfer<CityDTO> responseTransfer;
        City city=cityRepository.getCityByUuid(uuid);

        if(city==null){
            responseTransfer= ResponseTransfer.<CityDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<CityDTO>builder()
                            .message("error city not found with this uuid")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.<CityDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<CityDTO>builder()
                            .message("accept city successful returned with this uuid")
                            .data(cityDTOSerializer.toCityDTO(city))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> editCity(final City city, final UUID regionUuid){

        final ResponseTransfer<?> responseTransfer;
        if(city.getUuid()==null){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .responseBody(ResponseBody.builder()
                            .message("error edited city uuid is invalid")
                            .build())
                    .build();

            return responseTransfer;
        }
        final Boolean isEdited=cityRepository.editCity(city.getUuid(), city.getName(), regionUuid);

        if(isEdited==null || !isEdited){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error city don't edited")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept city successful edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> removeCityByUuid(final UUID uuid){

        final ResponseTransfer<?> responseTransfer;
        final Boolean isRemoved=cityRepository.removeCityByUuid(uuid);

        if(isRemoved==null || !isRemoved){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error city don't removed")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept city successful removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<CityDTO>> getActiveCityDTOSByRegionUuid(final UUID regionUuid){

        final ResponseTransfer<List<CityDTO>> responseTransfer;
        final List<City>cities=cityRepository.getCitiesByRegionUuid(regionUuid);
        final List<CityDTO>cityDTOS=new LinkedList<>();

        if(cities!=null){
            for(City city : cities){
                cityDTOS.add(cityDTOSerializer.toCityDTOOnlyUuidAndNameWithRegion(city));
            }
        }
        responseTransfer= ResponseTransfer.<List<CityDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<CityDTO>>builder()
                        .message("accept all founded cities successful returned")
                        .data(cityDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<CityDTO>> getActiveCityDTOS(){

        final ResponseTransfer<List<CityDTO>> responseTransfer;
        final List<City>cities=cityRepository.getActiveCities();
        final List<CityDTO>cityDTOS=new LinkedList<>();

        if(cities!=null){
            for(City city : cities){
                cityDTOS.add(cityDTOSerializer.toCityDTOOnlyUuidAndNameWithRegion(city));
            }
        }
        responseTransfer= ResponseTransfer.<List<CityDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<CityDTO>>builder()
                        .message("accept all founded cities successful returned")
                        .data(cityDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationCityByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=cityRepository.switchActivationCityByUuid(uuid, isActive);

        if(isSwitched==null || !isSwitched){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error city activation don't switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept city activation successful switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
