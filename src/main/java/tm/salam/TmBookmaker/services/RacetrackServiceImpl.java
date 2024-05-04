package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.RacetrackRepository;
import tm.salam.TmBookmaker.dtoes.serializers.RacetrackDTOSerializer;
import tm.salam.TmBookmaker.dtoes.models.RacetrackDTO;
import tm.salam.TmBookmaker.helpers.PaginationBuilder;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Racetrack;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Service
public class RacetrackServiceImpl implements RacetrackService {

    private final RacetrackRepository racetrackRepository;
    private final PaginationBuilder paginationBuilder;
    private final RacetrackDTOSerializer racetrackDTOSerializer;

    public RacetrackServiceImpl(RacetrackRepository racetrackRepository, PaginationBuilder paginationBuilder,
                                RacetrackDTOSerializer racetrackDTOSerializer) {
        this.racetrackRepository = racetrackRepository;
        this.paginationBuilder = paginationBuilder;
        this.racetrackDTOSerializer = racetrackDTOSerializer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addRacetrack(final Racetrack racetrack, final UUID cityUuid, final UUID regionUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isAdded= racetrackRepository.addRacetrack(racetrack.getName(), cityUuid, regionUuid);

        if(isAdded==null || !isAdded){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error racetrack don't added")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept racetrack successful created")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer>getNumberRacetracksBySearchKey(String searchKey){

        final ResponseTransfer<Integer>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberRacetracks=racetrackRepository.getNumberRacetracksBySearchKey(searchKey);

        responseTransfer=ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("accept number racetracks successful returned")
                        .data(numberRacetracks)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<RacetrackDTO>> getRacetrackDTOSBySearchKey(final int page, final int size,
                                                                            String searchKey, SortOption[] sortOptions){

        final ResponseTransfer<List<RacetrackDTO>>responseTransfer;

        sortOptions=parseRacetrackSortOption(sortOptions);
        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        List<Racetrack>racetracks= racetrackRepository.getRacetracksBySearchKey(pageable, searchKey);
        List<RacetrackDTO>racetrackDTOS=new LinkedList<>();

        if(racetracks!=null){
            for(Racetrack racetrack:racetracks){
                racetrackDTOS.add(racetrackDTOSerializer.toRacetrackDTOGeneral(racetrack));
            }
        }
        responseTransfer= ResponseTransfer.<List<RacetrackDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<RacetrackDTO>>builder()
                        .message("accept racetracks successful returned")
                        .data(racetrackDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parseRacetrackSortOption(final SortOption[] sortOptions){

        if(sortOptions==null){
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
                case "cityName" -> {
                    sortOption.setField("city.name");
                    parsedSortOptions.add(sortOption);
                }
            }
        }

        return parsedSortOptions.toArray(SortOption[] ::new);
    }

    @Override
    public ResponseTransfer<RacetrackDTO> getRacetrackDTOByUuid(final UUID uuid){

        final ResponseTransfer<RacetrackDTO>responseTransfer;
        final Racetrack racetrack= racetrackRepository.getRacetrackByUuid(uuid);

        if(racetrack==null){
            responseTransfer= ResponseTransfer.<RacetrackDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<RacetrackDTO>builder()
                            .message("error racetrack not found")
                            .build())
                    .build();
        }else {
            responseTransfer= ResponseTransfer.<RacetrackDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<RacetrackDTO>builder()
                            .message("accept racetrack successful returned")
                            .data(racetrackDTOSerializer.toRacetrackDTO(racetrack))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>editRacetrack(final Racetrack racetrack, final UUID cityUuid, final UUID regionUuid){

        final ResponseTransfer<?>responseTransfer;
        final boolean isEdited= racetrackRepository.editRacetrack(racetrack.getUuid(), racetrack.getName(),
                cityUuid, regionUuid);

        if(!isEdited){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.builder()
                            .message("error racetrack don't edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.builder()
                            .message("accept racetrack successful edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>removeRacetrackByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isRemoved= racetrackRepository.removeRacetrackByUuid(uuid);

        if(isRemoved==null || !isRemoved){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error racetrack don't removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept racetrack successful removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<RacetrackDTO>>getActiveRacetrackDTOS(){

        final ResponseTransfer<List<RacetrackDTO>>responseTransfer;
        final List<Racetrack>racetracks=racetrackRepository.getActiveRacetracks();
        final List<RacetrackDTO>racetrackDTOS=new LinkedList<>();

        if(racetracks!=null){
            for(Racetrack racetrack:racetracks){
                racetrackDTOS.add(racetrackDTOSerializer.toRacetrackDTOOnlyUuidAndName(racetrack));
            }
        }
        responseTransfer= ResponseTransfer.<List<RacetrackDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<RacetrackDTO>>builder()
                        .message("accept all racetracks successful returned")
                        .data(racetrackDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationRacetrackByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=racetrackRepository.switchActivationRacetrackByUuid(uuid, isActive);

        if(isSwitched==null || !isSwitched){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error racetrack activation don't switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept racetrack activation successful switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
