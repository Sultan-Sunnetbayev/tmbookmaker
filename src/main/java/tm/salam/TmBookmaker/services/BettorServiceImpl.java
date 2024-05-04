package tm.salam.TmBookmaker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import tm.salam.TmBookmaker.daoes.BettorRepository;
import tm.salam.TmBookmaker.dtoes.models.BettorDTO;
import tm.salam.TmBookmaker.dtoes.serializers.BettorDTOSerializer;
import tm.salam.TmBookmaker.helpers.*;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Bettor;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.Duration;
import java.util.*;

@Service
public class BettorServiceImpl implements BettorService {

    private final BettorRepository bettorRepository;
    private final BettorDTOSerializer bettorDTOSerializer;
    private final SmsService smsService;
    private final PaginationBuilder paginationBuilder;
    private final PasswordEncoder passwordEncoder;


    @Value("${default.activation-code.life-time}")
    private Duration defaultActivationCodeLifeTime;
    @Value("${default.limit-sms}")
    private int defaultLimitSms;

    public BettorServiceImpl(BettorRepository bettorRepository, BettorDTOSerializer bettorDTOSerializer,
                             SmsService smsService, PaginationBuilder paginationBuilder, PasswordEncoder passwordEncoder) {
        this.bettorRepository = bettorRepository;
        this.bettorDTOSerializer = bettorDTOSerializer;
        this.smsService = smsService;
        this.paginationBuilder = paginationBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>registrateBettor(final Bettor bettor, final UUID roleUuid){

        ResponseTransfer<?>responseTransfer;
        Random random=new Random();
        String activationCode=String.format("%04d", random.nextInt(10000));
        final int limitSmsSavedBettor=bettorRepository.registrateBettor(bettor.getPhoneNumber(),
                passwordEncoder.encode(bettor.getPhoneNumber()), roleUuid, defaultLimitSms, activationCode,
                bettor.getConfirmAge18(), bettor.getAcceptancePrivacyPolicy(), new Date());

        if(limitSmsSavedBettor<=0) {
            responseTransfer = ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bettor don't added SMS limit has been exceeded")
                            .build())
                    .build();

            return responseTransfer;
        }
        try {
            smsService.sendSms(bettor.getPhoneNumber(), activationCode);
            bettorRepository.decrementBettorLimitSms(bettor.getPhoneNumber(), 1);
        } catch (ConnectException | ResourceAccessException connectException) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message(connectException.getMessage())
                            .build())
                    .build();

            return responseTransfer;
        }

        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.CREATED)
                .responseBody(ResponseBody.builder()
                        .message("accept bettor successful added")
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<UUID>confirmBettorByActivationCode(final Bettor bettor){

        final ResponseTransfer<UUID>responseTransfer;
        final UUID uuidConfirmedBettor=bettorRepository.confirmBettorByActivationCode(bettor.getPhoneNumber(),
                bettor.getActivationCode(), defaultActivationCodeLifeTime.toString());

        if(uuidConfirmedBettor==null){
            responseTransfer=ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.<UUID>builder()
                            .message("error activation code don't equals or not valid")
                            .build())
                    .build();
        }else{
            bettor.setUuid(uuidConfirmedBettor);
            responseTransfer=ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.<UUID>builder()
                            .message("accept bettor successful activated")
                            .data(uuidConfirmedBettor)
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>activateBettor(final Bettor bettor){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isEdited=bettorRepository.activateBettor(bettor.getUuid(), bettor.getUsername());

        if(isEdited==null || !isEdited){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bettor don't activated")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bettor successful activated")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<Integer> getNumberBettorsBySearchKey(String searchKey){

        final ResponseTransfer<Integer>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        final int numberBettors=bettorRepository.getNumberBettorsBySearchKey(searchKey);

        responseTransfer=ResponseTransfer.<Integer>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Integer>builder()
                        .message("number bettors by search key")
                        .data(numberBettors)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<BettorDTO>>getBettorDTOSBySearchKey(final int page, final int size, String searchKey,
                                                                     SortOption[] sortOptions){

        final ResponseTransfer<List<BettorDTO>>responseTransfer;

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        sortOptions=parseBettorSortOption(sortOptions);
        final Pageable pageable= paginationBuilder.buildPagination(page, size, sortOptions);
        final List<Bettor>bettors=bettorRepository.getBettorsBySearchKey(pageable, searchKey);
        List<BettorDTO>bettorDTOS=new LinkedList<>();

        if(bettors!=null){
            for(Bettor bettor:bettors){
                bettorDTOS.add(bettorDTOSerializer.toBettorDTOGeneral(bettor));
            }
        }
        responseTransfer=ResponseTransfer.<List<BettorDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<BettorDTO>>builder()
                        .message("accept bettors successful returned")
                        .data(bettorDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    private SortOption[] parseBettorSortOption(final SortOption[] sortOptions){

        if(sortOptions==null){
            return null;
        }
        List<SortOption> parsedSortOptions=new LinkedList<>();

        for (SortOption sortOption : sortOptions) {
            switch (sortOption.getField()) {
                case "username", "deposit", "winnings" -> {
                    parsedSortOptions.add(sortOption);
                }
                case "phoneNumber" -> {
                    sortOption.setField("phone_number");
                    parsedSortOptions.add(sortOption);
                }
                case "cashOut" -> {
                    sortOption.setField("cash_out");
                    parsedSortOptions.add(sortOption);
                }
            }
        }

        return parsedSortOptions.toArray(SortOption[]::new);
    }

    @Override
    public ResponseTransfer<Map<String, String>>getFinanceStateBettors(String searchKey){

        final ResponseTransfer<Map<String, String>>responseTransfer;
        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);

        final String[] financeStateBettors=bettorRepository.getFinanceStateBettors(searchKey).split(",");
        final Map<String, String>financeState=new HashMap<>();

        financeState.put("deposits", financeStateBettors[0]);
        financeState.put("winnings", financeStateBettors[1]);
        financeState.put("cashOuts", financeStateBettors[2]);

        responseTransfer=ResponseTransfer.<Map<String, String>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<Map<String, String>>builder()
                        .message("sum finance state bettors")
                        .data(financeState)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<BettorDTO>getBettorDTOByUuid(final UUID uuid){

        final ResponseTransfer<BettorDTO>responseTransfer;
        final Bettor bettor=bettorRepository.getBettorByUuid(uuid);

        if(bettor==null){
            responseTransfer= ResponseTransfer.<BettorDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<BettorDTO>builder()
                            .message("error bettor not found")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.<BettorDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<BettorDTO>builder()
                            .message("accept bettor successful returned")
                            .data(bettorDTOSerializer.toBettorDTOGeneral(bettor))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>editBettor(final Bettor bettor){

        final ResponseTransfer<?>responseTransfer;

        if(isBettorExistsByPhoneNumber(bettor.getUuid(), bettor.getPhoneNumber())){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error other bettor exists with phone number")
                            .build())
                    .build();

            return responseTransfer;
        }
        final Boolean isEdited=bettorRepository.editBettor(bettor.getUuid(), bettor.getUsername(), bettor.getPhoneNumber(),
                bettor.getDeposit());

        if(isEdited==null || !isEdited){
            responseTransfer= ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bettor don't edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bettor successful edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    private boolean isBettorExistsByPhoneNumber(final UUID uuid, final String phoneNumber){

        return bettorRepository.isBettorExistsByPhoneNumber(uuid, phoneNumber);
    }

    @Override
    @Transactional
    public ResponseTransfer<?>removeBettorByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isRemoved=bettorRepository.removeBettorByUuid(uuid);

        if(isRemoved==null || !isRemoved){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bettor don't removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bettor successful removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> topUpDeposit(final Bettor bettor){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isDepositToppedUp=bettorRepository.topUpDeposit(bettor.getUuid(), bettor.getDeposit());

        if(isDepositToppedUp==null || !isDepositToppedUp){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bettor deposit don't top up")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bettor deposit topped up")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<BettorDTO>getBettorDTOProfileByUuidForAppClient(final UUID uuid){

        final ResponseTransfer<BettorDTO>responseTransfer;
        final Bettor bettor=bettorRepository.getBettorByUuid(uuid);

        if(bettor==null){
            responseTransfer= ResponseTransfer.<BettorDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<BettorDTO>builder()
                            .message("error bettor not found")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.<BettorDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<BettorDTO>builder()
                            .message("accept bettor successful returned")
                            .data(bettorDTOSerializer.toBettorDTOProfileForAppClient(bettor))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>deactivateBettorByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isBettorDeactivated=bettorRepository.deactivateBettorByUuid(uuid);

        if(Boolean.TRUE.equals(isBettorDeactivated)){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bettor successful deactivated")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bettor don't deactivated")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<?>checkDepositValidityForPlacementBets(final UUID bettorUuid, final BigDecimal betAmount) {

        final ResponseTransfer<?>responseTransfer;

        if (bettorRepository.checkDepositValidityForBets(bettorUuid, betAmount)) {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.OK)
                    .build();
        }else {
            responseTransfer = ResponseTransfer.builder()
                    .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                    .responseBody(ResponseBody.builder()
                            .message("error the amount of bets exceeds the deposit bettor")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
