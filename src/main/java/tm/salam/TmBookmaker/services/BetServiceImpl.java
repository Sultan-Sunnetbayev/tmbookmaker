package tm.salam.TmBookmaker.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.TmBookmaker.daoes.BetHorseRepository;
import tm.salam.TmBookmaker.daoes.BetRepository;
import tm.salam.TmBookmaker.daoes.BettorRepository;
import tm.salam.TmBookmaker.daoes.MoneyRepository;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.BetStatusType;
import tm.salam.TmBookmaker.helpers.types.TransactionStatus;
import tm.salam.TmBookmaker.helpers.types.TransactionType;
import tm.salam.TmBookmaker.models.Bet;
import tm.salam.TmBookmaker.models.Horse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class BetServiceImpl implements BetService{

    private final BetRepository betRepository;
    private final MoneyRepository moneyRepository;
    private final BetHorseRepository betHorseRepository;
    private final BettorRepository bettorRepository;

    public BetServiceImpl(BetRepository betRepository, MoneyRepository moneyRepository,
                          BetHorseRepository betHorseRepository, BettorRepository bettorRepository) {
        this.betRepository = betRepository;
        this.moneyRepository = moneyRepository;
        this.betHorseRepository = betHorseRepository;
        this.bettorRepository = bettorRepository;
    }

    @Override
    @Transactional
    public ResponseTransfer<?> addBet(final Bet bet, Horse[] chosenHorses, final UUID betOptionUuid,
                                      final UUID horseRaceUuid, final UUID bettorUuid){

        final ResponseTransfer<?>responseTransfer;
        final UUID transactedMoneyUuid=moneyRepository.addMoney(bet.getTransactedMoney().getAmount(), LocalTime.now(),
                LocalDate.now(), bet.getTransactedMoney().getTransactionType().name(), TransactionStatus.BET_PLACED.name());

        if(transactedMoneyUuid==null){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.FAILED_DEPENDENCY)
                    .responseBody(ResponseBody.builder()
                            .message("error payment for the bet didn't go through")
                            .build())
                    .build();

            return responseTransfer;
        }
        final UUID savedBetUuid=betRepository.addBet(betOptionUuid, horseRaceUuid, bettorUuid, transactedMoneyUuid,
                bet.getOdds(), BetStatusType.PENDING.name());

        if(savedBetUuid==null){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.EXPECTATION_FAILED)
                    .responseBody(ResponseBody.builder()
                            .message("error bets don't added")
                            .build())
                    .build();

            return responseTransfer;
        }
        for(Horse horse:chosenHorses){
            betHorseRepository.addBetHorse(savedBetUuid, horse.getUuid(), horse.getPlace());
        }
        if(Objects.equals(TransactionType.ACCOUNT_WALLET, bet.getTransactedMoney().getTransactionType())) {
            bettorRepository.decrementDepositOnBetPlacementByBettorUuid(bettorUuid, bet.getTransactedMoney().getAmount());
        }
        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.CREATED)
                .responseBody(ResponseBody.builder()
                        .message("accept bets successful added")
                        .build())
                .build();

        return responseTransfer;
    }

}
