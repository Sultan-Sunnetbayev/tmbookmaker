package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.BetHorseRepository;
import tm.salam.TmBookmaker.daoes.BetRepository;
import tm.salam.TmBookmaker.daoes.HorseRaceRepository;
import tm.salam.TmBookmaker.daoes.HorseRepository;
import tm.salam.TmBookmaker.dtoes.models.BetHorseDTO;
import tm.salam.TmBookmaker.dtoes.models.HorseDTO;
import tm.salam.TmBookmaker.dtoes.serializers.*;
import tm.salam.TmBookmaker.dtoes.models.BetDTO;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceDTO;
import tm.salam.TmBookmaker.helpers.PaginationBuilder;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.*;

import java.util.*;

@Service
public class HorseRaceServiceImpl implements HorseRaceService {

    private final HorseRaceRepository horseRaceRepository;
    private final BetRepository betRepository;
    private final HorseRepository horseRepository;
    private final BetHorseRepository betHorseRepository;
    private final HorseRaceDTOSerializer horseRaceDTOSerializer;
    private final BetDTOSerializer betDTOSerializer;
    private final BetOptionDTOSerializer betOptionDTOSerializer;
    private final BetHorseDTOSerializer betHorseDTOSerializer;
    private final HorseDTOSerializer horseDTOSerializer;
    private final PaginationBuilder paginationBuilder;

    public HorseRaceServiceImpl(HorseRaceRepository horseRaceRepository, BetRepository betRepository,
                                HorseRepository horseRepository, BetHorseRepository betHorseRepository,
                                HorseRaceDTOSerializer horseRaceDTOSerializer, BetDTOSerializer betDTOSerializer,
                                BetOptionDTOSerializer betOptionDTOSerializer, BetHorseDTOSerializer betHorseDTOSerializer,
                                HorseDTOSerializer horseDTOSerializer, PaginationBuilder paginationBuilder) {
        this.horseRaceRepository = horseRaceRepository;
        this.betRepository = betRepository;
        this.horseRepository = horseRepository;
        this.betHorseRepository = betHorseRepository;
        this.horseRaceDTOSerializer = horseRaceDTOSerializer;
        this.betDTOSerializer = betDTOSerializer;
        this.betOptionDTOSerializer = betOptionDTOSerializer;
        this.betHorseDTOSerializer = betHorseDTOSerializer;
        this.horseDTOSerializer = horseDTOSerializer;
        this.paginationBuilder = paginationBuilder;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>addHorseRace(final HorseRace horseRace, final UUID dataFileUuid,
                                              final UUID[] betOptions, final UUID horseRaceEventUuid){

        final ResponseTransfer<?>responseTransfer;
        final UUID savedHorseRaceUuid=horseRaceRepository.addHorseRace(horseRace.getDate(),
                horseRace.getTime(), dataFileUuid, horseRace.getHorses().size(), betOptions, horseRaceEventUuid);

        if(savedHorseRaceUuid==null){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race don't added")
                            .build())
                    .build();
        }else{
            for(Horse horse:horseRace.getHorses()){
                horseRepository.addHorse(horse.getNumber(), horse.isActive(), savedHorseRaceUuid);
            }
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept horse race successful added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer>getNumberHorseRacesBySearchKey(String searchKey, final boolean isActive,
                                                                   final UUID horseRaceEventUuid){

        final ResponseTransfer<Integer>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberHorseRace=horseRaceRepository.getNumberHorseRacesBySearchKey(searchKey,
                isActive, horseRaceEventUuid);

        responseTransfer= ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("accept number horse races successful returned")
                        .data(numberHorseRace)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseRaceDTO>>getHorseRaceDTOSBySearchKey(final int page, final int size, String searchKey,
                                                                           final boolean isActive, SortOption[] sortOptions,
                                                                           final UUID horseRaceEventUuid){

        final ResponseTransfer<List<HorseRaceDTO>>responseTransfer;

        sortOptions=parseHorseRaceSortOption(sortOptions);
        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final List<HorseRace>horseRaces=horseRaceRepository.getHorseRacesBySearchKey(pageable, searchKey, isActive, horseRaceEventUuid);
        final List<HorseRaceDTO>horseRaceDTOS=new LinkedList<>();

        System.out.println(horseRaces.size());
        if(horseRaces!=null){
            for(HorseRace horseRace:horseRaces){
                horseRaceDTOS.add(horseRaceDTOSerializer.toHorseRaceDTOGeneral(horseRace));
            }
        }
        responseTransfer= ResponseTransfer.<List<HorseRaceDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseRaceDTO>>builder()
                        .message("accept all founded horse race successful returned")
                        .data(horseRaceDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parseHorseRaceSortOption(final SortOption[] sortOptions){

        if(sortOptions==null){
            return null;
        }
        List<SortOption>parsedSortOptions=new LinkedList<>();

        for(SortOption sortOption:sortOptions){
            switch(sortOption.getField()){
                case "time", "date" -> {
                    parsedSortOptions.add(sortOption);
                }
                case "numberHorses" -> {
                    sortOption.setField("number_horses");
                    parsedSortOptions.add(sortOption);
                }
                case "numberBets" -> {
                    sortOption.setField("number_bets");
                    parsedSortOptions.add(sortOption);
                }
            }
        }

        return parsedSortOptions.toArray(SortOption[] ::new);
    }

    @Override
    public ResponseTransfer<HorseRaceDTO>getHorseRaceDTOByUuid(final UUID uuid){

        final ResponseTransfer<HorseRaceDTO>responseTransfer;
        final HorseRace horseRace=horseRaceRepository.getHorseRaceByUuid(uuid);

        if(horseRace==null){
            responseTransfer= ResponseTransfer.<HorseRaceDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<HorseRaceDTO>builder()
                            .message("error horse race not found")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.<HorseRaceDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<HorseRaceDTO>builder()
                            .message("accept horse race successful founded")
                            .data(horseRaceDTOSerializer.toHorseRaceDTO(horseRace))
                            .build())
                    .build();

        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationHorseRaceByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=horseRaceRepository.switchHorseRaceActivation(uuid, isActive);

        if(isSwitched==null || !isSwitched){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race activation don't switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept horse race activation successful switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchHorseRaceBetPermission(final UUID horseRaceUuid, final boolean betPermission){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=horseRaceRepository.switchHorseRaceBetPermission(horseRaceUuid, betPermission);

        if (Boolean.TRUE.equals(isSwitched)) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept horse race bet permission switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race bet permission don't switched")
                            .build())
                    .build();

        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseRaceDTO>>getHorseRaceDTOSByHorseRaceEventUuidForBettorBets(final UUID horseRaceEventUuid,
                                                                                                 final UUID bettorUuid){

        final ResponseTransfer<List<HorseRaceDTO>>responseTransfer;
        final List<HorseRace>horseRaces=horseRaceRepository.getHorseRacesByHorseRaceEventUuid(horseRaceEventUuid);
        final List<HorseRaceDTO>horseRaceDTOS=new LinkedList<>();

        if(horseRaces!=null){
            for(HorseRace horseRace:horseRaces){
                List<BetDTO>betDTOS=createOrGetBetDTOSForBettor(bettorUuid, horseRace.getUuid(), horseRace.getBetOptions());
                HorseRaceDTO horseRaceDTO= horseRaceDTOSerializer.toHorseRaceDTOForCreateBets(horseRace);
                horseRaceDTO.setBetDTOS(betDTOS);
                horseRaceDTOS.add(horseRaceDTO);
            }
        }

        responseTransfer=ResponseTransfer.<List<HorseRaceDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseRaceDTO>>builder()
                        .message("accept horse races successful returned")
                        .data(horseRaceDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private List<BetDTO>createOrGetBetDTOSForBettor(final UUID bettorUuid, final UUID horseRaceUuid,
                                                    final List<BetOption>betOptions){
        final Bet[] bets=betRepository.getBetsByBettorUuidAndHorseRaceUuid(bettorUuid, horseRaceUuid);
        final List<BetDTO>betDTOS=new LinkedList<>();

        if(bets==null){
            return betDTOS;
        }
        Map<Integer, BetDTO>betDTOMap=new LinkedHashMap<>();
        for(Bet bet:bets){
            betDTOMap.put(bet.getOdds(), betDTOSerializer.toBetDTO(bet));
        }
        for(BetOption betOption:betOptions){
            if(!betDTOMap.containsKey(betOption.getOdds())){
                betDTOMap.put(betOption.getOdds(), BetDTO.builder()
                        .betOptionDTO(betOptionDTOSerializer.toBetOptionDTO(betOption))
                        .build());
            }
        }

        return betDTOMap.values().stream().toList();
    }

    @Override
    public ResponseTransfer<?>isHorseRaceClosed(final UUID horseRaceUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isHorseRaceClosed=horseRaceRepository.isHorseRaceClosed(horseRaceUuid);

        if(!Boolean.TRUE.equals(isHorseRaceClosed)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.OK)
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race don't closed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>addResultHorseRace(final UUID horseRaceUuid, final Horse[] horses){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isHorseRaceClosed=horseRaceRepository.isHorseRaceClosed(horseRaceUuid);

        if(Boolean.TRUE.equals(isHorseRaceClosed)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error horse race don't closed")
                            .build())
                    .build();

            return responseTransfer;
        }
        for(Horse horse:horses){
            horseRepository.updateHorsePlace(horse.getUuid(), horse.getPlace());
            betHorseRepository.updateBetHorseState(horse.getUuid(), horse.getPlace());
        }
        betRepository.updateBetStatus(horseRaceUuid);
        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.ACCEPTED)
                .responseBody(ResponseBody.builder()
                        .message("accept result horse races added")
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<HorseRaceDTO>>getResultHorseRaceDTOSByHorseRaceEventUuidAndBettorUuid(final UUID horseRaceEventUuid,
                                                                                                       final UUID bettorUuid){
        final ResponseTransfer<List<HorseRaceDTO>>responseTransfer;
        List<HorseRace>horseRaces=horseRaceRepository.getHorseRacesByHorseRaceEventUuid(horseRaceEventUuid);

        if(horseRaces==null){
            horseRaces=new LinkedList<>();
        }
        List<UUID>horseRaceUuids=horseRaces.stream().map(HorseRace::getUuid).toList();
        Bet[] bets=betRepository.getBetsByBettorUuidAndHorseRaceUuids(bettorUuid, horseRaceUuids);
        Map<UUID, List<BetDTO>>betDTOMap=new LinkedHashMap<>();
        Map<UUID, BetHorseDTO>betHorseDTOMap=new HashMap<>();

        for(Bet bet:bets){
                List<BetDTO>betDTOS=betDTOMap.get(bet.getHorseRace().getUuid());
                if(betDTOS==null){
                    betDTOS=new LinkedList<>();
                }
                betDTOS.add(betDTOSerializer.toBetDTOOnlyTransactedMoney(bet));
                betDTOMap.put(bet.getHorseRace().getUuid(), betDTOS);
            for(BetHorse betHorse:bet.getBetHorses()){
                betHorseDTOMap.put(betHorse.getHorse().getUuid(), betHorseDTOSerializer.toBetHorseDTOOnlyPlaceAndState(betHorse));
            }
        }
        List<HorseRaceDTO>resultHorseRaceDTOS=new LinkedList<>();

        for(HorseRace horseRace:horseRaces){
            List<HorseDTO>horseDTOS=new LinkedList<>();

            for(Horse horse:horseRace.getHorses()){
                HorseDTO horseDTO=horseDTOSerializer.toHorseDTOForResult(horse);
                horseDTO.setBetHorseDTO(betHorseDTOMap.get(horse.getUuid()));
                horseDTOS.add(horseDTO);
            }
            HorseRaceDTO horseRaceDTO=horseRaceDTOSerializer.toHorseRaceDTOForResult(horseRace);

            horseRaceDTO.setHorseDTOS(horseDTOS);
            horseRaceDTO.setBetDTOS(betDTOMap.get(horseRaceDTO.getUuid()));
            resultHorseRaceDTOS.add(horseRaceDTO);
        }
        responseTransfer= ResponseTransfer.<List<HorseRaceDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<HorseRaceDTO>>builder()
                        .message("accept result horse races successful returned")
                        .data(resultHorseRaceDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<HorseRaceDTO>getHorseRaceDTOByUuidForCreateBet(final UUID horseRaceUuid){

        final ResponseTransfer<HorseRaceDTO>responseTransfer;
        final HorseRace horseRace=horseRaceRepository.getHorseRaceByUuid(horseRaceUuid);

        if(horseRace==null){
            responseTransfer= ResponseTransfer.<HorseRaceDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<HorseRaceDTO>builder()
                            .message("error horse race not found with this uuid")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.<HorseRaceDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<HorseRaceDTO>builder()
                            .message("accept horse race successful returned for create bet")
                            .data(horseRaceDTOSerializer.toHorseRaceDTOForCreateBet(horseRace))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
