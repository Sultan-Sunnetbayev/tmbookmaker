package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.PopulatedPlaceRepository;
import tm.salam.TmBookmaker.dtoes.serializers.PopulatedPlaceDTOSerializer;
import tm.salam.TmBookmaker.dtoes.models.PopulatedPlaceDTO;
import tm.salam.TmBookmaker.helpers.PaginationBuilder;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.PopulatedPlace;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class PopulatedPlaceServiceImpl implements PopulatedPlaceService {

    private final PopulatedPlaceRepository populatedPlaceRepository;
    private final PaginationBuilder paginationBuilder;
    private final PopulatedPlaceDTOSerializer populatedPlaceDTOSerializer;

    public PopulatedPlaceServiceImpl(PopulatedPlaceRepository populatedPlaceRepository, PaginationBuilder paginationBuilder,
                                     PopulatedPlaceDTOSerializer populatedPlaceDTOSerializer) {
        this.populatedPlaceRepository = populatedPlaceRepository;
        this.paginationBuilder = paginationBuilder;
        this.populatedPlaceDTOSerializer = populatedPlaceDTOSerializer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addPopulatedPlace(final PopulatedPlace populatedPlace, final UUID cityUuid,
                                                 final UUID regionUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isAdded=populatedPlaceRepository.addPopulatedPlace(populatedPlace.getName(), cityUuid, regionUuid);

        if(Boolean.TRUE.equals(isAdded)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept populated place successful created")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error populated place don't added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer>getNumberPopulatedPlacesBySearchKey(String searchKey){

        final ResponseTransfer<Integer>responseTransfer;
        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);

        final int numberPopulatedPlaces=populatedPlaceRepository.getNumberPopulatedPlacesBySearchKey(searchKey);

        responseTransfer= ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("accept number populated places by search key")
                        .data(numberPopulatedPlaces)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<PopulatedPlaceDTO>>getPopulatedPlaceDTOSBySearchKey(final int page, final int size,
                                                                                     String searchKey, SortOption[] sortOptions){

        final ResponseTransfer<List<PopulatedPlaceDTO>> responseTransfer;
        sortOptions =parsePopulatedPlaceColumns(sortOptions);

        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final List<PopulatedPlace>populatedPlaces=populatedPlaceRepository.getPopulatedPlacesBySearchKey(pageable, searchKey);
        final List<PopulatedPlaceDTO>populatedPlaceDTOS=new LinkedList<>();

        if(populatedPlaces!=null){
            for(PopulatedPlace populatedPlace:populatedPlaces){
                populatedPlaceDTOS.add(populatedPlaceDTOSerializer.toPopulatedPlaceDTOGeneral(populatedPlace));
            }
        }
        responseTransfer= ResponseTransfer.<List<PopulatedPlaceDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<PopulatedPlaceDTO>>builder()
                        .message("accept founded populated places successful returned")
                        .data(populatedPlaceDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parsePopulatedPlaceColumns(final SortOption[] sortOptions) {

        if (sortOptions == null) {
            return null;
        }
        List<SortOption>parsedSortOptions=new LinkedList<>();

        for(SortOption sortOption:sortOptions){
            switch(sortOption.getField()){
                case "name" -> {
                    parsedSortOptions.add(sortOption);
                }
                case "cityName" -> {
                    sortOption.setField("city.name");
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
    public ResponseTransfer<PopulatedPlaceDTO>getPopulatedPlaceDTOByUuid(final UUID uuid){

        final ResponseTransfer<PopulatedPlaceDTO>responseTransfer;
        final PopulatedPlace populatedPlace=populatedPlaceRepository.getPopulatedPlaceByUuid(uuid);

        if(populatedPlace==null){
            responseTransfer= ResponseTransfer.<PopulatedPlaceDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<PopulatedPlaceDTO>builder()
                            .message("error populated place not found")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.<PopulatedPlaceDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<PopulatedPlaceDTO>builder()
                            .message("accept populated places successful founded")
                            .data(populatedPlaceDTOSerializer.toPopulatedPlaceDTO(populatedPlace))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>editPopulatedPlace(final PopulatedPlace populatedPlace, final UUID cityUuid,
                                                 final UUID regionUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isEdited=populatedPlaceRepository.editPopulatedPlace(populatedPlace.getUuid(), populatedPlace.getName(),
                cityUuid, regionUuid);

        if(Boolean.TRUE.equals(isEdited)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept populated place successful edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error populated place don't edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>removePopulatedPlaceByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isRemoved=populatedPlaceRepository.removePopulatedPlaceByUuid(uuid);

        if (Boolean.TRUE.equals(isRemoved)) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept populated place successful removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error populated place don't removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationPopulatedPlaceByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=populatedPlaceRepository.switchActivationPopulatedPlaceByUuid(uuid, isActive);

        if(Boolean.TRUE.equals(isSwitched)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept city activation successful switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error populated place activation don't switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<PopulatedPlaceDTO>>getActivePopulatedPlaceDTOS(final UUID cityUuid, final UUID regionUuid){

        final ResponseTransfer<List<PopulatedPlaceDTO>>responseTransfer;
        final List<PopulatedPlace>populatedPlaces;

        if(cityUuid!=null && regionUuid!=null) {
            populatedPlaces = populatedPlaceRepository.getActivePopulatedPlacesByCityUuidAndRegionUuid(cityUuid, regionUuid);
        } else if(regionUuid==null && cityUuid==null){
            populatedPlaces = populatedPlaceRepository.getActivePopulatedPlaces();
        } else if(regionUuid==null){
            populatedPlaces=populatedPlaceRepository.getActivePopulatedPlacesByCityUuid(cityUuid);
        }else {
            populatedPlaces=populatedPlaceRepository.getActivePopulatedPlacesRegionUuid(regionUuid);
        }
        final List<PopulatedPlaceDTO>populatedPlaceDTOS=new LinkedList<>();

        if(populatedPlaces!=null){
            for(PopulatedPlace populatedPlace:populatedPlaces){
                populatedPlaceDTOS.add(populatedPlaceDTOSerializer.toPopulatedPlaceDTOOnlyUuidAndNameWithCityAndRegion(populatedPlace));
            }
        }
        responseTransfer= ResponseTransfer.<List<PopulatedPlaceDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<PopulatedPlaceDTO>>builder()
                        .message("accept active populated places successful returned")
                        .data(populatedPlaceDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

}
