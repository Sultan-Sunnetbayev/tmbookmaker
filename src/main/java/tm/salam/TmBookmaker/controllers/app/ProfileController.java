package tm.salam.TmBookmaker.controllers.app;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.BankDTO;
import tm.salam.TmBookmaker.dtoes.models.CardDTO;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Card;
import tm.salam.TmBookmaker.services.BankService;
import tm.salam.TmBookmaker.services.BettorService;
import tm.salam.TmBookmaker.services.CardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/app/profile")
public class ProfileController {

    private final BettorService bettorService;
    private final BankService bankService;
    private final CardService cardService;
    private final JsonParser jsonParser;

    public ProfileController(BettorService bettorService, BankService bankService, CardService cardService,
                             JsonParser jsonParser) {
        this.bettorService = bettorService;
        this.bankService = bankService;
        this.cardService = cardService;
        this.jsonParser = jsonParser;
    }

    @PostMapping(path = "/get-profile", params = {"bettorUuid"}, produces = "application/json")
    public ResponseEntity<?>getProfile(@RequestParam("bettorUuid") UUID bettorUuid){

        final ResponseTransfer<?> responseTransfer=bettorService.getBettorDTOProfileByUuidForAppClient(bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/logout", params = {"bettorUuid"}, produces = "application/json")
    public ResponseEntity<?>logout(@RequestParam("bettorUuid") UUID bettorUuid){

        final ResponseTransfer<?> responseTransfer=bettorService.deactivateBettorByUuid(bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-banks", produces = "application/json")
    public ResponseEntity<?>getBanksBySearchKey(){

        final ResponseTransfer<List<BankDTO>>responseTransfer=bankService.getActiveBankDTOS();

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-card", produces = "application/json")
    public ResponseEntity<?>addCard(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID bettorUuid=UUID.fromString(jsonObject.getString("bettorUuid"));
        final UUID bankUuid=UUID.fromString(jsonObject.getString("bankUuid"));
        final Card card=jsonParser.fromJson(jsonObject.getJSONObject("card").toString(), Card.class);

        responseTransfer=cardService.addCard(card, bankUuid, bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-cards", params = {"bettorUuid"}, produces = "application/json")
    public ResponseEntity<?>getCards(@RequestParam("bettorUuid")UUID bettorUuid){

        final ResponseTransfer<List<CardDTO>>responseTransfer=cardService.getCardsByBettorUuid(bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-card", produces = "application/json")
    public ResponseEntity<?>editCard(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID bankUuid=UUID.fromString(jsonObject.getString("bankUuid"));
        final Card card=jsonParser.fromJson(jsonObject.getJSONObject("card").toString(), Card.class);

        responseTransfer=cardService.editCard(card, bankUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-card", params = {"cardUuid"}, produces = "application/json")
    public ResponseEntity<?>removeCard(@RequestParam("cardUuid")UUID cardUuid){

        final ResponseTransfer<?>responseTransfer=cardService.removeCardByUuid(cardUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
