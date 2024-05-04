package tm.salam.TmBookmaker.controllers.web;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.BetOption;
import tm.salam.TmBookmaker.services.BetOptionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/settings/bet-option")
public class BetOptionController {

    private final BetOptionService betOptionService;
    private final JsonParser jsonParser;

    public BetOptionController(BetOptionService betOptionService, JsonParser jsonParser) {
        this.betOptionService = betOptionService;
        this.jsonParser = jsonParser;
    }

    @PostMapping(path = "/add-bet-option", produces = "application/json")
    public ResponseEntity<?> addBetOption(@RequestBody BetOption betOption){

        final ResponseTransfer<?> responseTransfer=betOptionService.addBetOption(betOption);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-number/bet-options", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>getNumberBetOptions(@RequestParam(value = "searchKey", defaultValue = "")String searchKey){

        final ResponseTransfer<Integer>responseTransfer=betOptionService.getNumberBetOptionsBySearchKey(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-bet-options", params = {"page", "size", "searchKey"}, produces = "application/json")
    public ResponseEntity<?>getBetOptionBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                    @RequestParam(value = "size", defaultValue = "10")int size,
                                                    @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                                    @RequestBody(required = false) String requestBody){

        final ResponseTransfer<?>responseTransfer;
        SortOption[] sortOptions=null;

        if(requestBody!=null) {
            final JSONObject jsonObject = new JSONObject(requestBody);
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.get("sort").toString(), SortOption[].class);
        }
        responseTransfer=betOptionService.getBetOptionDTOSBySearchKey(page, size, searchKey, sortOptions);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-bet-option", params = {"betOptionUuid"}, produces = "application/json")
    public ResponseEntity<?>getBetOption(@RequestParam("betOptionUuid")UUID betOptionUuid){

        final ResponseTransfer<?>responseTransfer=betOptionService.getBetOptionDTOByUuid(betOptionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-bet-option", produces = "application/json")
    public ResponseEntity<?>editBetOption(@RequestBody @Validated BetOption betOption){

        final ResponseTransfer<?>responseTransfer=betOptionService.editBetOption(betOption);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-bet-option", params = {"betOptionUuid"}, produces = "application/json")
    public ResponseEntity<?>removeBetOption(@RequestParam("betOptionUuid")UUID betOptionUuid){

        final ResponseTransfer<?>responseTransfer=betOptionService.removeBetOptionByUuid(betOptionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/bet-option", produces = "application/json")
    public ResponseEntity<?>switchActivationBetOption(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID betOptionUuid=UUID.fromString(jsonObject.getString("uuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=betOptionService.switchActivationBetOptionByUuid(betOptionUuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
