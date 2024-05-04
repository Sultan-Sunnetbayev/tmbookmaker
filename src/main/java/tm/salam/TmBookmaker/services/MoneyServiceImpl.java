package tm.salam.TmBookmaker.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.MoneyRepository;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Money;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class MoneyServiceImpl implements MoneyService {

    private final MoneyRepository moneyRepository;

    public MoneyServiceImpl(MoneyRepository moneyRepository) {
        this.moneyRepository = moneyRepository;
    }

    @Override
    @Transactional
    public ResponseTransfer<UUID> addMoney(final Money money){

        final ResponseTransfer<UUID>responseTransfer;
        final UUID savedMoneyUuid=moneyRepository.addMoney(money.getAmount(), LocalTime.now(), LocalDate.now(),
                money.getTransactionType().name(), money.getTransactionStatus().name());

        if(savedMoneyUuid!=null){
            responseTransfer= ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.CREATED)
                    .responseBody(ResponseBody.<UUID>builder()
                            .data(savedMoneyUuid)
                            .build())
                    .build();

        }else{
            responseTransfer= ResponseTransfer.<UUID>builder()
                    .httpStatus(HttpStatus.FAILED_DEPENDENCY)
                    .responseBody(ResponseBody.<UUID>builder()
                            .message("error payment for the bet didn't go through")
                            .build())
                    .build();
        }

        return responseTransfer;
    }

}
