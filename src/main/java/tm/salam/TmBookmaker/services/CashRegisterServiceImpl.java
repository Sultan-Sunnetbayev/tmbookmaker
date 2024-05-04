package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.CashRegisterRepository;
import tm.salam.TmBookmaker.dtoes.models.CashRegisterDTO;
import tm.salam.TmBookmaker.dtoes.serializers.CashRegisterDTOSerializer;
import tm.salam.TmBookmaker.helpers.PaginationBuilder;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.CashRegister;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class CashRegisterServiceImpl implements CashRegisterService{

    private final UserService userService;
    private final CashRegisterRepository cashRegisterRepository;
    private final CashRegisterDTOSerializer cashRegisterDTOSerializer;
    private final PaginationBuilder paginationBuilder;

    public CashRegisterServiceImpl(UserService userService, CashRegisterRepository cashRegisterRepository,
                                   CashRegisterDTOSerializer cashRegisterDTOSerializer, PaginationBuilder paginationBuilder) {
        this.userService = userService;
        this.cashRegisterRepository = cashRegisterRepository;
        this.cashRegisterDTOSerializer = cashRegisterDTOSerializer;
        this.paginationBuilder = paginationBuilder;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addCashRegister(final CashRegister cashRegister, final UUID populatedPlaceUuid,
                                               final UUID cityUuid, final UUID regionUuid, final UUID cashierRoleUuid){

        final ResponseTransfer<UUID>responseTransferUserService=userService.addUser(cashRegister.getCashier(), cashierRoleUuid);

        if(!responseTransferUserService.getHttpStatus().is2xxSuccessful()){
            return responseTransferUserService;
        }
        final ResponseTransfer<?>responseTransfer;
        final Boolean isAdded=cashRegisterRepository.addCashRegister(cashRegister.getNumber(),
                responseTransferUserService.getResponseBody().getData(), populatedPlaceUuid, cityUuid, regionUuid);

        if(Boolean.TRUE.equals(isAdded)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept cash register successful added")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error cash register don't added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer>getNumberCashRegistersBySearchKey(String searchKey){

        final ResponseTransfer<Integer>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberCashRegisters=cashRegisterRepository.getNumberCashRegistersBySearchKey(searchKey);

        responseTransfer= ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("accept number cash registers successful returned")
                        .data(numberCashRegisters)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<CashRegisterDTO>>getCashRegisterDTOSBySearchKey(final int page, final int size,
                                                                                 String searchKey, SortOption[] sortOptions){

        ResponseTransfer<List<CashRegisterDTO>>responseTransfer;

        sortOptions =parseCashRegisterColumns(sortOptions);
        final Pageable pageable=paginationBuilder.buildPagination(page, size, sortOptions);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final List<CashRegister>cashRegisters=cashRegisterRepository.getCashRegistersBySearchKey(pageable, searchKey);
        final List<CashRegisterDTO>cashRegisterDTOS=new LinkedList<>();

        if(cashRegisters!=null){
            for(CashRegister cashRegister:cashRegisters){
                cashRegisterDTOS.add(cashRegisterDTOSerializer.toCashRegisterDTOGeneral(cashRegister));
            }
        }
        responseTransfer=ResponseTransfer.<List<CashRegisterDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<CashRegisterDTO>>builder()
                        .message("accept founded cash registers successful returned")
                        .data(cashRegisterDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parseCashRegisterColumns(final SortOption[] sortOptions) {

        if (sortOptions == null) {
            return null;
        }
        List<SortOption> parsedSortOptions = new LinkedList<>();

        for (SortOption sortOption : sortOptions) {
            switch (sortOption.getField()) {
                case "number" -> {
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
                case "populatedPlaceName" -> {
                    sortOption.setField("populatedPlace.name");
                    parsedSortOptions.add(sortOption);
                }
                case "cashierName" -> {
                    sortOption.setField("cashier.name");
                    parsedSortOptions.add(sortOption);
                }
                case "cashierSurname" -> {
                    sortOption.setField("cashier.surname");
                    parsedSortOptions.add(sortOption);
                }
            }
        }

        return parsedSortOptions.toArray(SortOption[]::new);
    }

    @Override
    public ResponseTransfer<CashRegisterDTO>getCashRegisterDTOByUuid(final UUID uuid){

        final ResponseTransfer<CashRegisterDTO>responseTransfer;
        final CashRegister cashRegister=cashRegisterRepository.getCashRegisterByUuid(uuid);

        if(cashRegister==null){
            responseTransfer= ResponseTransfer.<CashRegisterDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<CashRegisterDTO>builder()
                            .message("error cash register not found with this uuid")
                            .build())
                    .build();
        }else{
            responseTransfer= ResponseTransfer.<CashRegisterDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<CashRegisterDTO>builder()
                            .message("accept cash register successful returned")
                            .data(cashRegisterDTOSerializer.toCashRegisterDTO(cashRegister))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<?>editCashRegister(final CashRegister cashRegister, final UUID populatedPlaceUuid,
                                               final UUID cityUuid, final UUID regionUuid){

        ResponseTransfer<?>responseTransfer=userService.editUser(cashRegister.getCashier());

        if(!responseTransfer.getHttpStatus().is2xxSuccessful()){

            return responseTransfer;
        }

        final Boolean isEdited=cashRegisterRepository.editCashRegister(cashRegister.getUuid(), cashRegister.getNumber(),
                populatedPlaceUuid, cityUuid, regionUuid);

        if (Boolean.TRUE.equals(isEdited)) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept cash register successful edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error cash register don't edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>markCashRegisterAsRemoved(final UUID cashRegisterUuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean markedAsRemoved=cashRegisterRepository.markCashRegisterAsRemoved(cashRegisterUuid);

        if (Boolean.TRUE.equals(markedAsRemoved)) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept cash register successful removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error cash register don't removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>switchActivationCashRegisterByUuid(final UUID uuid, final boolean isActive){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isSwitched=cashRegisterRepository.switchActivationCashRegisterByUuid(uuid, isActive);

        if (Boolean.TRUE.equals(isSwitched)) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept cash register activation successful switched")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error cash register activation don't switched")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
