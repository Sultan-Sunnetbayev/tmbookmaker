package tm.salam.TmBookmaker.controllers.web;

import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.*;
import tm.salam.TmBookmaker.forms.FormCreateBetStep2;
import tm.salam.TmBookmaker.forms.FormCreateBetStep3;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.helpers.types.TransactionType;
import tm.salam.TmBookmaker.models.Bet;
import tm.salam.TmBookmaker.models.Bettor;
import tm.salam.TmBookmaker.models.Horse;
import tm.salam.TmBookmaker.services.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/web/bettor")
public class BettorController {

    private final BettorService bettorService;
    private final RoleService roleService;
    private final HorseRaceEventService horseRaceEventService;
    private final HorseRaceService horseRaceService;
    private final HorseService horseService;
    private final BetService betService;
    private final BetOptionService betOptionService;
    private  final JsonParser jsonParser;

    private final String roleName="ROLE_BETTOR";

    public BettorController(BettorService bettorService, RoleService roleService,
                            HorseRaceEventService horseRaceEventService, HorseRaceService horseRaceService,
                            HorseService horseService, BetService betService, BetOptionService betOptionService,
                            JsonParser jsonParser) {
        this.bettorService = bettorService;
        this.roleService = roleService;
        this.horseRaceEventService = horseRaceEventService;
        this.horseRaceService = horseRaceService;
        this.horseService = horseService;
        this.betService = betService;
        this.betOptionService = betOptionService;
        this.jsonParser = jsonParser;
    }

    @PostMapping(path = "/add-bettor", produces = "application/json")
    public ResponseEntity<?> addBettor(@RequestBody @Validated Bettor bettor){

        final ResponseTransfer<UUID>responseTransferRole=roleService.getRoleUuidByName(roleName);

        if(!responseTransferRole.getHttpStatus().is2xxSuccessful()){

            return ResponseEntity.status(responseTransferRole.getHttpStatus()).body(responseTransferRole.getResponseBody());
        }
        ResponseTransfer<?>responseTransfer=bettorService.registrateBettor(bettor,
                responseTransferRole.getResponseBody().getData());

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/confirm-bettor", produces = "application/json")
    public ResponseEntity<?>confirmBettor(@RequestBody Bettor bettor){

        final ResponseTransfer<UUID>responseTransfer=bettorService.confirmBettorByActivationCode(bettor);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/activate-bettor", produces = "application/json")
    public ResponseEntity<?>activateBettor(@RequestBody Bettor bettor){

        ResponseTransfer<?>responseTransfer=bettorService.activateBettor(bettor);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/number-bettors", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>getNumberBettorsBySearchKey(@RequestParam(value = "searchKey", defaultValue = "")String searchKey){

        final ResponseTransfer<Integer> responseTransfer=bettorService.getNumberBettorsBySearchKey(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-bettors", params = {"page", "size", "searchKey"}, produces="application/json")
    public ResponseEntity<?>getBettorsBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                  @RequestParam(value = "size", defaultValue = "10")int size,
                                                  @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                                  @RequestBody(required = false) String requestBody){

        final ResponseTransfer<List<BettorDTO>> responseTransfer;
        SortOption[] sortOptions=null;

        if(requestBody!=null) {
            final JSONObject jsonObject = new JSONObject(requestBody);
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.get("sort").toString(), SortOption[].class);
        }

        responseTransfer=bettorService.getBettorDTOSBySearchKey(page, size, searchKey, sortOptions);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/finance-state/bettors", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>financeStateBettors(@Param("searchKey")String searchKey){

        final ResponseTransfer<?>responseTransfer=bettorService.getFinanceStateBettors(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-bettor", params = {"bettorUuid"}, produces = "application/json")
    public ResponseEntity<?>getBettor(@RequestParam("bettorUuid")UUID bettorUuid){

        final ResponseTransfer<BettorDTO>responseTransfer=bettorService.getBettorDTOByUuid(bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-bettor", produces = "application/json")
    public ResponseEntity<?>editBettor(@RequestBody Bettor bettor){

        final ResponseTransfer<?>responseTransfer=bettorService.editBettor(bettor);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-bettor", params = {"bettorUuid"}, produces = "application/json")
    public ResponseEntity<?>removeBettor(@RequestParam("bettorUuid")UUID bettorUuid){

        ResponseTransfer<?>responseTransfer=bettorService.removeBettorByUuid(bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "top-up/deposit", produces = "application/json")
    public ResponseEntity<?>topUpDeposit(@RequestBody Bettor bettor){

        ResponseTransfer<?>responseTransfer=bettorService.topUpDeposit(bettor);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-last/horse-race-events", produces = "application/json")
    public ResponseEntity<?>getLastHorseRaceEvents(){

        final ResponseTransfer<List<HorseRaceEventDTO>>responseTransfer=horseRaceEventService.getLastHorseRaceEventDTOS();

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-horse-races", produces = "application/json")
    public ResponseEntity<?>getHorseRaces(@RequestBody String requestBody){

        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID horseRaceEventUuid=UUID.fromString(jsonObject.getString("horseRaceEventUuid"));
        final ResponseTransfer<HorseRaceEventDTO>
                responseTransferHorseRaceEventService=horseRaceEventService.getHorseRaceEventDTOByUuid(horseRaceEventUuid);
        if(!responseTransferHorseRaceEventService.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity
                    .status(responseTransferHorseRaceEventService.getHttpStatus())
                    .body(responseTransferHorseRaceEventService.getResponseBody());
        }
        final UUID bettorUuid=UUID.fromString(jsonObject.getString("bettorUuid"));
        final ResponseTransfer<List<HorseRaceDTO>>responseTransferHorseRaceService;

        responseTransferHorseRaceService=horseRaceService.getHorseRaceDTOSByHorseRaceEventUuidForBettorBets(horseRaceEventUuid, bettorUuid);

        ResponseBody<FormCreateBetStep2>
                responseBody=ResponseBody.<FormCreateBetStep2>builder()
                .message(responseTransferHorseRaceService.getResponseBody().getMessage())
                .data(FormCreateBetStep2.builder()
                                .horseRaceEventDTO(responseTransferHorseRaceEventService.getResponseBody().getData())
                                .horseRaceDTOS(responseTransferHorseRaceService.getResponseBody().getData())
                                .build()
                )
                .build();

        return ResponseEntity.status(responseTransferHorseRaceService.getHttpStatus()).body(responseBody);
    }

    @PostMapping(path = "/get-horse-race/with-bet-option", produces = "application/json")
    public ResponseEntity<?>getHorsesRaceForCreateBet(@RequestBody String requestBody){

        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID betOptionUuid=UUID.fromString(jsonObject.getString("betOptionUuid"));
        final ResponseTransfer<BetOptionDTO>
                responseTransferBetOptionService=betOptionService.getBetOptionDTOByUuid(betOptionUuid);

        if(!responseTransferBetOptionService.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity
                    .status(responseTransferBetOptionService.getHttpStatus())
                    .body(responseTransferBetOptionService.getResponseBody());
        }
        final UUID horseRaceEventUuid=UUID.fromString(jsonObject.getString("horseRaceEventUuid"));
        final ResponseTransfer<HorseRaceEventDTO>
                responseTransferHorseRaceEventService=horseRaceEventService.getHorseRaceEventDTOByUuid(horseRaceEventUuid);
        if(!responseTransferHorseRaceEventService.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity
                    .status(responseTransferHorseRaceEventService.getHttpStatus())
                    .body(responseTransferHorseRaceEventService.getResponseBody());
        }
        final UUID horseRaceUuid=UUID.fromString(jsonObject.getString("horseRaceUuid"));
        final ResponseTransfer<HorseRaceDTO>
                responseTransferHorseRaceService=horseRaceService.getHorseRaceDTOByUuidForCreateBet(horseRaceUuid);

        if(!responseTransferHorseRaceService.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity
                    .status(responseTransferHorseRaceService.getHttpStatus())
                    .body(responseTransferHorseRaceService.getResponseBody());
        }
        ResponseBody<FormCreateBetStep3>responseBody= ResponseBody.<FormCreateBetStep3>builder()
                .message(responseTransferHorseRaceService.getResponseBody().getMessage())
                .data(FormCreateBetStep3.builder()
                        .horseRaceEventDTO(responseTransferHorseRaceEventService.getResponseBody().getData())
                        .horseRaceDTO(responseTransferHorseRaceService.getResponseBody().getData())
                        .betOptionDTO(responseTransferBetOptionService.getResponseBody().getData())
                        .build()
                )
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping(path = "/add-bets", produces = "application/json")
    public ResponseEntity<?>addBets(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID bettorUuid=UUID.fromString(jsonObject.getString("bettorUuid"));
        final UUID horseRaceUuid=UUID.fromString(jsonObject.getString("horseRaceUuid"));
        final UUID betOptionUuid=UUID.fromString(jsonObject.getString("betOptionUuid"));
        final Horse[] chosenHorses=jsonParser.fromJsonAsArray(jsonObject.getJSONArray("chosenHorses").toString(),
                Horse[].class);
        final Bet bet=jsonParser.fromJson(jsonObject.getJSONObject("bet").toString(), Bet.class);

        assert bet.getTransactedMoney()!=null;
        bet.getTransactedMoney().setTransactionType(TransactionType.CASH);
        responseTransfer=betService.addBet(bet, chosenHorses, betOptionUuid, horseRaceUuid, bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
