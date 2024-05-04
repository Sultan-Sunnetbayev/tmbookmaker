package tm.salam.TmBookmaker.controllers.app;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceDTO;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.TransactionType;
import tm.salam.TmBookmaker.models.Bet;
import tm.salam.TmBookmaker.models.Horse;
import tm.salam.TmBookmaker.services.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/app/main")
public class MainController {

    private final HorseRaceEventService horseRaceEventService;
    private final HorseRaceService horseRaceService;
    private final HorseService horseService;
    private final BetService betService;
    private final BettorService bettorService;
    private final JsonParser jsonParser;

    public MainController(HorseRaceEventService horseRaceEventService, HorseRaceService horseRaceService,
                          HorseService horseService, BetService betService, BettorService bettorService, JsonParser jsonParser) {
        this.horseRaceEventService = horseRaceEventService;
        this.horseRaceService = horseRaceService;
        this.horseService = horseService;
        this.betService = betService;
        this.bettorService = bettorService;
        this.jsonParser = jsonParser;
    }

    @GetMapping(path = "/get-last/horse-race-events", produces = "application/json")
    public ResponseEntity<?> getLastHorseRaceEvents(){

        ResponseTransfer<List<HorseRaceEventDTO>>responseTransfer=horseRaceEventService.getLastHorseRaceEventDTOS();

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-horse-races", produces = "application/json")
    public ResponseEntity<?>getHorseRaces(@RequestBody String requestBody){

        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID horseRaceEventUuid=UUID.fromString(jsonObject.getString("horseRaceEventUuid"));
        final UUID bettorUuid=UUID.fromString(jsonObject.getString("bettorUuid"));
        final ResponseTransfer<List<HorseRaceDTO>>responseTransfer;

        responseTransfer=horseRaceService.getHorseRaceDTOSByHorseRaceEventUuidForBettorBets(horseRaceEventUuid, bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-horses-choice", params = {"horseRaceUuid"}, produces = "application/json")
    public ResponseEntity<?>getHorses(@RequestParam("horseRaceUuid")UUID horseRaceUuid){

        final ResponseTransfer<?>responseTransfer=horseService.getHorsesByHorseRaceUuidForCreateBet(horseRaceUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-bets", produces = "application/json")
    public ResponseEntity<?>addBets(@RequestBody String requestBody){

        ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID bettorUuid=UUID.fromString(jsonObject.getString("bettorUuid"));
        final Bet bet=jsonParser.fromJson(jsonObject.getJSONObject("bet").toString(), Bet.class);

        assert bet.getTransactedMoney()!=null;
        responseTransfer=bettorService.checkDepositValidityForPlacementBets(bettorUuid, bet.getTransactedMoney().getAmount());
        if(!responseTransfer.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
        }
        final UUID horseRaceUuid=UUID.fromString(jsonObject.getString("horseRaceUuid"));
        final UUID betOptionUuid=UUID.fromString(jsonObject.getString("betOptionUuid"));
        final Horse[] chosenHorses=jsonParser.fromJsonAsArray(jsonObject.getJSONArray("chosenHorses").toString(),
                Horse[].class);

        bet.getTransactedMoney().setTransactionType(TransactionType.ACCOUNT_WALLET);
        responseTransfer=betService.addBet(bet, chosenHorses, betOptionUuid, horseRaceUuid, bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
