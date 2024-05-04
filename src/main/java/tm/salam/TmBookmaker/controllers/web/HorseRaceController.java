package tm.salam.TmBookmaker.controllers.web;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.BetOptionDTO;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceDTO;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Horse;
import tm.salam.TmBookmaker.models.HorseRace;
import tm.salam.TmBookmaker.services.BetOptionService;
import tm.salam.TmBookmaker.services.HorseRaceService;
import tm.salam.TmBookmaker.services.HorseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/web/horse-race")
public class HorseRaceController {

    private final HorseRaceService horseRaceService;
    private final HorseService horseService;
    private final BetOptionService betOptionService;
    private final JsonParser jsonParser;

    public HorseRaceController(HorseRaceService horseRaceService, HorseService horseService, BetOptionService betOptionService,
                               JsonParser jsonParser) {
        this.horseRaceService = horseRaceService;
        this.horseService = horseService;
        this.betOptionService = betOptionService;
        this.jsonParser = jsonParser;
    }

    @GetMapping(path = "/get-bet-options", produces = "application/json")
    public ResponseEntity<?>getBetOptions(){

        final ResponseTransfer<List<BetOptionDTO>>responseTransfer=betOptionService.getActiveBetOptionDTOS();

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-horse-race", produces = "application/json")
    public ResponseEntity<?>addHorseRace(@RequestBody String requestBody){

        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID horseRaceEventUuid=UUID.fromString(jsonObject.getString("horseRaceEventUuid"));
        final UUID dataFileUuid=UUID.fromString(jsonObject.getString("dataFileUuid"));
        final UUID[] betOptionUuids=jsonParser.fromJsonAsArray(jsonObject.getJSONArray("betOptionUuids").toString(), UUID[].class);
        final HorseRace horseRace=jsonParser.fromJson(jsonObject.getJSONObject("horseRace").toString(), HorseRace.class);

        assert horseRace.getHorses()!=null;
        final ResponseTransfer<?> responseTransfer=horseRaceService.addHorseRace(horseRace, dataFileUuid,
                betOptionUuids, horseRaceEventUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-number/horse-races", produces = "application/json")
    public ResponseEntity<?>getNumberHorseRacesBySearchKey(@RequestBody String requestBody){

        final JSONObject jsonObject=new JSONObject(requestBody);
        final String searchKey=jsonObject.getString("searchKey");
        final UUID horseRaceEventUuid=jsonParser.fromJson(jsonObject.getString("horseRaceEventUuid"), UUID.class);
        final boolean isActive=jsonObject.getBoolean("isActive");
        final ResponseTransfer<Integer>responseTransfer=horseRaceService.getNumberHorseRacesBySearchKey(searchKey, isActive, horseRaceEventUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-horse-races", params = {"page", "size", "searchKey"}, produces = "application/json")
    public ResponseEntity<?>getHorseRacesBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                     @RequestParam(value = "size", defaultValue = "10")int size,
                                                     @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                                     @RequestBody String requestBody){
        final ResponseTransfer<List<HorseRaceDTO>> responseTransfer;
        SortOption[] sortOptions=null;
        final JSONObject jsonObject = new JSONObject(requestBody);
        final UUID horseRaceEventUuid=UUID.fromString(jsonObject.getString("horseRaceEventUuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        if(jsonObject.has("sort")) {
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.getJSONArray("sort").toString(), SortOption[].class);
        }

        responseTransfer=horseRaceService.getHorseRaceDTOSBySearchKey(page, size, searchKey, isActive, sortOptions, horseRaceEventUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-horse-race", params = {"horseRaceUuid"}, produces = "application/json")
    public ResponseEntity<?>gerHorseRace(@RequestParam("horseRaceUuid")UUID horseRaceUuid){

        final ResponseTransfer<HorseRaceDTO>responseTransfer=horseRaceService.getHorseRaceDTOByUuid(horseRaceUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/horse-race", produces = "application/json")
    public ResponseEntity<?>switchActivationHorseRace(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID horseRaceUuid=jsonParser.fromJson(jsonObject.getString("horseRaceUuid"), UUID.class);
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=horseRaceService.switchActivationHorseRaceByUuid(horseRaceUuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-result/horse-race", produces = "application/json")
    public ResponseEntity<?>addResultHoseRace(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID horseRaceUuid=UUID.fromString(jsonObject.getString("uuid"));
        final Horse[] horses=jsonParser.fromJson(jsonObject.getJSONArray("horses").toString(), Horse[].class);

        responseTransfer=horseRaceService.addResultHorseRace(horseRaceUuid, horses);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/horse", produces = "application/json")
    public ResponseEntity<?>switchActivationHorse(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final Horse[] horses=jsonParser.fromJsonAsArray(jsonObject.getJSONArray("horses").toString(), Horse[].class);

        responseTransfer=horseService.switchActivationHorses(horses);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/bet-permission", produces = "application/json")
    public ResponseEntity<?>switchActivationBetPermission(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID horseRaceUuid=UUID.fromString(jsonObject.getString("horseRaceUuid"));
        final boolean betPermission=jsonObject.getBoolean("betPermission");

        responseTransfer=horseRaceService.switchHorseRaceBetPermission(horseRaceUuid, betPermission);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
