package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.HorseRaceEventRepository;
import tm.salam.TmBookmaker.daoes.RacetrackRepository;
import tm.salam.TmBookmaker.dtoes.serializers.HorseRaceEventDTOSerializer;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;
import tm.salam.TmBookmaker.helpers.PaginationBuilder;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.HorseRaceEvent;
import tm.salam.TmBookmaker.models.Racetrack;

import java.time.LocalDate;
import java.util.*;

@Service
public class HorseRaceEventServiceImpl implements HorseRaceEventService {

    private final HorseRaceEventRepository horseRaceEventRepository;
    private final RacetrackRepository racetrackRepository;
    private final HorseRaceEventDTOSerializer horseRaceEventDTOSerializer;
    private final PaginationBuilder paginationBuilder;

    public HorseRaceEventServiceImpl(HorseRaceEventRepository horseRaceEventRepository,
                                     RacetrackRepository racetrackRepository,
                                     HorseRaceEventDTOSerializer horseRaceEventDTOSerializer,
                                     PaginationBuilder paginationBuilder) {
        this.horseRaceEventRepository = horseRaceEventRepository;
        this.racetrackRepository = racetrackRepository;
        this.horseRaceEventDTOSerializer = horseRaceEventDTOSerializer;
        this.paginationBuilder = paginationBuilder;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addHorseRaceEvent(final HorseRaceEvent horseRaceEvent, final UUID racetrackUuid){

        System.out.println(horseRaceEvent.getTime() + " " + horseRaceEvent.getDate());
        final ResponseTransfer<?>responseTransfer;
        final Boolean isAdded= horseRaceEventRepository.addHorseRaceEvent(horseRaceEvent.getName(), horseRaceEvent.getTime(),
                horseRaceEvent.getDate(), racetrackUuid);

        if(isAdded==null || !isAdded){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race event don't added")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept horse race event successful created")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer>getNumberHorseRaceEventsBySearchKey(String searchKey, boolean isActive){

        final ResponseTransfer<Integer>responseTransfer;
        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberHorseRaceEvents=horseRaceEventRepository.getNumberHorseRaceEventsBySearchKey(searchKey, isActive);

        responseTransfer= ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("accept horse race events number")
                        .data(numberHorseRaceEvents)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseRaceEventDTO>> getHorseRaceEventDTOSBySearchKey(final int page, final int size,
                                                                                      String searchKey, SortOption[] sortOptions,
                                                                                      boolean isActive){

        final ResponseTransfer<List<HorseRaceEventDTO>>responseTransfer;

        sortOptions=parseHorseRaceEventOption(sortOptions);
        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        List<HorseRaceEvent>horseRaceEvents= horseRaceEventRepository.getHorseRaceEventsBySearchKey(pageable, searchKey, isActive);
        List<HorseRaceEventDTO>horseRaceEventDTOS=new LinkedList<>();

        if(horseRaceEvents!=null){
            for(HorseRaceEvent horseRaceEvent:horseRaceEvents){
                horseRaceEventDTOS.add(horseRaceEventDTOSerializer.toHorseRaceEventDTOGeneral(horseRaceEvent));
            }
        }
        responseTransfer= ResponseTransfer.<List<HorseRaceEventDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseRaceEventDTO>>builder()
                        .message("accept horse race events successful returned")
                        .data(horseRaceEventDTOS)
                        .build())
                .build();

        return responseTransfer;
    }


    private SortOption[] parseHorseRaceEventOption(final SortOption[] sortOptions){

        if(sortOptions==null){
            return null;
        }
        List<SortOption>parsedSortOption=new LinkedList<>();

        for(SortOption sortOption:sortOptions){
            switch(sortOption.getField()){
                case "name", "time", "date" -> {
                    parsedSortOption.add(sortOption);
                }
                case "numberHorseRaces" -> {
                    sortOption.setField("number_horse_races");
                    parsedSortOption.add(sortOption);
                }
                case "numberBeats" -> {
                    sortOption.setField("number_beats");
                    parsedSortOption.add(sortOption);
                }
            }
        }

        return parsedSortOption.toArray(SortOption[] ::new);
    }

    @Override
    public ResponseTransfer<HorseRaceEventDTO> getHorseRaceEventDTOByUuid(final UUID uuid){

        final ResponseTransfer<HorseRaceEventDTO>responseTransfer;
        final HorseRaceEvent horseRaceEvent= horseRaceEventRepository.getHorseRaceEventByUuid(uuid);

        if(horseRaceEvent==null){
            responseTransfer= ResponseTransfer.<HorseRaceEventDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<HorseRaceEventDTO>builder()
                            .message("error horse race event not found")
                            .build())
                    .build();
        }else {
            responseTransfer= ResponseTransfer.<HorseRaceEventDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<HorseRaceEventDTO>builder()
                            .message("accept horse race event successful returned")
                            .data(horseRaceEventDTOSerializer.toHorseRaceEventDTO(horseRaceEvent))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>editHorseRaceEvent(final HorseRaceEvent horseRaceEvent, final UUID racetrackUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isEdited= horseRaceEventRepository.editHorseRaceEvent(horseRaceEvent.getUuid(), horseRaceEvent.getName(),
                horseRaceEvent.getTime(), horseRaceEvent.getDate(), racetrackUuid);

        if(isEdited==null || !isEdited){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race event don't edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.builder()
                            .message("accept horse race event successful edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>removeHorseRaceEventByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isRemoved= horseRaceEventRepository.removeHorseRaceEventByUuid(uuid);

        if(isRemoved==null || !isRemoved){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race event don't removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept horse race event successful removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationHorseRaceEventByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=horseRaceEventRepository.switchActivationHorseRaceEventByUuid(uuid, isActive);

        if(isSwitched==null || !isSwitched){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race event activation don't switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept horse race event activation successful switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseRaceEventDTO>>getActiveHorseRaceEventDTOS(){

        final ResponseTransfer<List<HorseRaceEventDTO>>responseTransfer;
        final List<HorseRaceEvent>horseRaceEvents=horseRaceEventRepository.getActiveHorseRaceEvents();
        final List<HorseRaceEventDTO>horseRaceEventDTOS=new LinkedList<>();

        if(horseRaceEvents!=null){
            for(HorseRaceEvent horseRaceEvent:horseRaceEvents){
                horseRaceEventDTOS.add(horseRaceEventDTOSerializer.toHorseRaceEventDTOForBet(horseRaceEvent));
            }
        }
        responseTransfer=ResponseTransfer.<List<HorseRaceEventDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseRaceEventDTO>>builder()
                        .message("accept active horse race events successful returned")
                        .data(horseRaceEventDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseRaceEventDTO>>getLastHorseRaceEventDTOS(){

        final ResponseTransfer<List<HorseRaceEventDTO>>responseTransfer=ResponseTransfer.<List<HorseRaceEventDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseRaceEventDTO>>builder()
                        .message("accept founded last horse race events successful returned")
                        .build())
                .build();
        final List<Racetrack>racetracks=racetrackRepository.getActiveRacetracks();

        if(racetracks==null){
            responseTransfer.getResponseBody().setData(new ArrayList<>());

            return responseTransfer;
        }
        final List<HorseRaceEvent>horseRaceEvents=horseRaceEventRepository.getLastHorseRaceEvents();
        final Map<UUID, HorseRaceEventDTO>horseRaceEventDTOMap=new LinkedHashMap<>();

        for(Racetrack racetrack:racetracks){
            horseRaceEventDTOMap.put(racetrack.getUuid(), horseRaceEventDTOSerializer.toHorseRaceEventDTOForBet(racetrack));
        }
        if(horseRaceEvents==null){
            responseTransfer.getResponseBody().setData(horseRaceEventDTOMap.values().stream().toList());

            return responseTransfer;
        }
        for(HorseRaceEvent horseRaceEvent:horseRaceEvents){
            horseRaceEventDTOMap.put(horseRaceEvent.getRacetrack().getUuid(),
                    horseRaceEventDTOSerializer.toHorseRaceEventDTOForBet(horseRaceEvent));
        }
        responseTransfer.getResponseBody().setData(horseRaceEventDTOMap.values().stream().toList());

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseRaceEventDTO>>getHorseRaceEventDTOSByDate(final LocalDate date){

        final ResponseTransfer<List<HorseRaceEventDTO>>responseTransfer=ResponseTransfer.<List<HorseRaceEventDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseRaceEventDTO>>builder()
                        .message("accept founded last horse race events successful returned")
                        .build())
                .build();
        final List<HorseRaceEvent>horseRaceEvents=horseRaceEventRepository.getHorseRaceEventsByDate(date);
        final List<HorseRaceEventDTO>horseRaceEventDTOS=new LinkedList<>();

        final List<Racetrack>racetracks=racetrackRepository.getActiveRacetracks();
        final Map<UUID, HorseRaceEventDTO>horseRaceEventDTOMap=new LinkedHashMap<>();

        for(Racetrack racetrack:racetracks){
            horseRaceEventDTOMap.put(racetrack.getUuid(), horseRaceEventDTOSerializer.toHorseRaceEventDTOForBet(racetrack));
        }
        if(horseRaceEvents==null){
            responseTransfer.getResponseBody().setData(horseRaceEventDTOMap.values().stream().toList());

            return responseTransfer;
        }
        for(HorseRaceEvent horseRaceEvent:horseRaceEvents){
            horseRaceEventDTOMap.put(horseRaceEvent.getRacetrack().getUuid(),
                    horseRaceEventDTOSerializer.toHorseRaceEventDTOForBet(horseRaceEvent));
        }
        responseTransfer.getResponseBody().setData(horseRaceEventDTOMap.values().stream().toList());

        return responseTransfer;
    }

}
