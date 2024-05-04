package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.BetOptionRepository;
import tm.salam.TmBookmaker.dtoes.serializers.BetOptionDTOSerializer;
import tm.salam.TmBookmaker.dtoes.models.BetOptionDTO;
import tm.salam.TmBookmaker.helpers.PaginationBuilder;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.BetOption;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class BetOptionServiceImpl implements BetOptionService {

    private final BetOptionRepository betOptionRepository;
    private final PaginationBuilder paginationBuilder;
    private final BetOptionDTOSerializer betOptionDTOSerializer;

    public BetOptionServiceImpl(BetOptionRepository betOptionRepository, PaginationBuilder paginationBuilder,
                                BetOptionDTOSerializer betOptionDTOSerializer) {
        this.betOptionRepository = betOptionRepository;
        this.paginationBuilder = paginationBuilder;
        this.betOptionDTOSerializer = betOptionDTOSerializer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addBetOption(final BetOption betOption){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isAdded=betOptionRepository.addBetOption(betOption.getName(), betOption.getMinAmount(),
                betOption.getMaxAmount());

        if(isAdded==null || !isAdded){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error bet option don't added")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bet option successful added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer>getNumberBetOptionsBySearchKey(String searchKey){

        final ResponseTransfer<Integer>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberBetOption=betOptionRepository.getNumberBetOptionsBySearchKey(searchKey);

        responseTransfer= ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("accept number bet option successful returned")
                        .data(numberBetOption)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<BetOptionDTO>>getBetOptionDTOSBySearchKey(final int page, final int size, String searchKey,
                                                                           SortOption[] sortOptions){

        final ResponseTransfer<List<BetOptionDTO>>responseTransfer;

        sortOptions=parseBetOptionSortOption(sortOptions);
        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);
        final List<BetOption>betOptions=betOptionRepository.getBetOptionsBySearchKey(pageable, searchKey);
        final List<BetOptionDTO>betOptionDTOS=new LinkedList<>();

        if(betOptions!=null){
            for(BetOption betOption:betOptions){
               betOptionDTOS.add(betOptionDTOSerializer.toBetOptionDTOGeneral(betOption));
            }
        }
        responseTransfer= ResponseTransfer.<List<BetOptionDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<BetOptionDTO>>builder()
                        .message("accept founded bet options successful returned")
                        .data(betOptionDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parseBetOptionSortOption(final SortOption[] sortOptions){

        if(sortOptions==null){
            return null;
        }
        List<SortOption> parsedSortOptions=new LinkedList<>();

        for (SortOption sortOption : sortOptions) {
            switch (sortOption.getField()) {
                case "name" -> {
                    parsedSortOptions.add(sortOption);
                }
                case "minAmount" -> {
                    sortOption.setField("min_amount");
                    parsedSortOptions.add(sortOption);
                }
                case "maxAmount" -> {
                    sortOption.setField("max_amount");
                    parsedSortOptions.add(sortOption);
                }
            }
        }

        return parsedSortOptions.toArray(SortOption[]::new);
    }

    @Override
    public ResponseTransfer<BetOptionDTO>getBetOptionDTOByUuid(final UUID uuid){

        final ResponseTransfer<BetOptionDTO>responseTransfer;
        final BetOption betOption=betOptionRepository.getBetOptionByUuid(uuid);

        if(betOption==null){
            responseTransfer= ResponseTransfer.<BetOptionDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<BetOptionDTO>builder()
                            .message("error bet option not found")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.<BetOptionDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<BetOptionDTO>builder()
                            .message("accept bet option successful founded")
                            .data(betOptionDTOSerializer.toBetOptionDTO(betOption))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>editBetOption(final BetOption betOption){

        final ResponseTransfer<?>responseTransfer;
        final boolean isEdited=betOptionRepository.editBetOption(betOption.getUuid(), betOption.getName(),
                betOption.getMinAmount(), betOption.getMaxAmount());

        if(!isEdited){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bet option don't edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bet option successful edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>removeBetOptionByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isRemoved=betOptionRepository.removeBetOptionByUuid(uuid);

        if(isRemoved==null || !isRemoved){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bet option don't removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bet option successful removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationBetOptionByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=betOptionRepository.switchActivationBetOptionByUuid(uuid, isActive);

        if(isSwitched==null || !isSwitched){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bet option activation don't switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bet option activation successful switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<BetOptionDTO>>getActiveBetOptionDTOS(){

        final ResponseTransfer<List<BetOptionDTO>>responseTransfer;
        final List<BetOption>betOptions=betOptionRepository.getActiveBetOptions();
        final List<BetOptionDTO>betOptionDTOS=new LinkedList<>();

        if(betOptions!=null){
            for(BetOption betOption:betOptions){
                betOptionDTOS.add(betOptionDTOSerializer.toBetOptionDTOOnlyUuidAndName(betOption));
            }
        }
        responseTransfer= ResponseTransfer.<List<BetOptionDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<BetOptionDTO>>builder()
                        .message("accept active bet options successful returned")
                        .data(betOptionDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

}
