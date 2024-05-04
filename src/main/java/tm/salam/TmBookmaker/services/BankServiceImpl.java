package tm.salam.TmBookmaker.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.BankRepository;
import tm.salam.TmBookmaker.dtoes.models.BankDTO;
import tm.salam.TmBookmaker.dtoes.serializers.BankDTOSerializer;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Bank;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final BankDTOSerializer bankDTOSerializer;


    public BankServiceImpl(BankRepository bankRepository, BankDTOSerializer bankDTOSerializer) {
        this.bankRepository = bankRepository;
        this.bankDTOSerializer = bankDTOSerializer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addBank(final Bank bank){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isAdded=bankRepository.addBank(bank.getName());

        if(isAdded==null || !isAdded){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .responseBody(ResponseBody.builder()
                            .message("error bank don't added")
                            .build())
                    .build();
        }else {
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bank successful added")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<BankDTO>>getBankDTOSBySearchKey(final int page, final int size, String searchKey){

        final ResponseTransfer<List<BankDTO>>responseTransfer;
        final Pageable pageable= PageRequest.of(page, size);

        searchKey=searchKey.trim().toLowerCase(Locale.ROOT);
        List<Bank>banks=bankRepository.getBanksBySearchKey(pageable, searchKey);
        List<BankDTO>bankDTOS=new LinkedList<>();

        if(banks!=null){
            for(Bank bank:banks){
                bankDTOS.add(bankDTOSerializer.toBankDTO(bank));
            }
        }
        responseTransfer=ResponseTransfer.<List<BankDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<BankDTO>>builder()
                        .message("accept banks successful returned")
                        .data(bankDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<BankDTO>getBankDTOByUuid(final UUID uuid){

        final ResponseTransfer<BankDTO>responseTransfer;
        final Bank bank=bankRepository.getBankByUuid(uuid);

        if(bank==null){
            responseTransfer=ResponseTransfer.<BankDTO>builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .responseBody(ResponseBody.<BankDTO>builder()
                            .message("error bank not found with this uuid")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.<BankDTO>builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.<BankDTO>builder()
                            .message("accept bank successful returned")
                            .data(bankDTOSerializer.toBankDTO(bank))
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>editBank(final Bank bank){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isEdited=bankRepository.editBank(bank.getUuid(), bank.getName());

        if(isEdited==null || !isEdited){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bank don't edited")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.ACCEPTED)
                    .responseBody(ResponseBody.builder()
                            .message("accept bank successful edited")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer<?>removeBankByUuid(final UUID uuid){

        final ResponseTransfer<?>responseTransfer;
        final Boolean isRemoved=bankRepository.removeBankByUuid(uuid);

        if(isRemoved==null || !isRemoved){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bank don't removed")
                            .build())
                    .build();
        }else{
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.OK)
                    .responseBody(ResponseBody.builder()
                            .message("accept bank successful removed")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<List<BankDTO>>getActiveBankDTOS(){

        final ResponseTransfer<List<BankDTO>>responseTransfer;
        final List<Bank>banks=bankRepository.getActiveBanks();
        final List<BankDTO>bankDTOS=new LinkedList<>();

        if(banks!=null){
            for(Bank bank:banks){
                bankDTOS.add(bankDTOSerializer.toBankDTO(bank));
            }
        }
        responseTransfer= ResponseTransfer.<List<BankDTO>>builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.<List<BankDTO>>builder()
                        .message("accept active banks successful returned")
                        .data(bankDTOS)
                        .build())
                .build();

        return responseTransfer;
    }

}
