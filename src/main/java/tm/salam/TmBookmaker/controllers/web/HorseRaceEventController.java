package tm.salam.TmBookmaker.controllers.web;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;
import tm.salam.TmBookmaker.dtoes.models.RacetrackDTO;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.HorseRaceEvent;
import tm.salam.TmBookmaker.services.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/event/horse-race")
public class HorseRaceEventController {

    private final HorseRaceEventService horseRaceEventService;
    private final RacetrackService racetrackService;
    private final JsonParser jsonParser;

    public HorseRaceEventController(HorseRaceEventService horseRaceEventService, HorseRaceService horseRaceService,
                                    RacetrackService racetrackService, HorseService horseService, JsonParser jsonParser) {
        this.horseRaceEventService = horseRaceEventService;
        this.racetrackService = racetrackService;
        this.jsonParser = jsonParser;
    }

    @GetMapping(path = "/get-racetracks", produces = "application/json")
    public ResponseEntity<?>getRacetracks(){

        final ResponseTransfer<List<RacetrackDTO>>responseTransfer=racetrackService.getActiveRacetrackDTOS();

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-event", produces = "application/json")
    public ResponseEntity<?> addHorseRace(@RequestBody String requestBody) {

        final ResponseTransfer<?> responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final HorseRaceEvent horseRaceEvent=jsonParser.fromJson(jsonObject.getJSONObject("horseRaceEvent").toString(),
                HorseRaceEvent.class);
        final UUID racetrackUuid=UUID.fromString(jsonObject.getString("racetrackUuid"));

        responseTransfer=horseRaceEventService.addHorseRaceEvent(horseRaceEvent, racetrackUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-numbers", produces = "application/json")
    public ResponseEntity<?>getNumberHorseRaceEventsBySearchKey(@RequestBody String requestBody){

        final ResponseTransfer<Integer>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final String searchKey=jsonObject.getString("searchKey");
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=horseRaceEventService.getNumberHorseRaceEventsBySearchKey(searchKey, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-events", params = {"page", "size", "searchKey"}, produces = "application/json")
    public ResponseEntity<?>getHorseRaceEventsBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                          @RequestParam(value = "size", defaultValue = "10")int size,
                                                          @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                                          @RequestBody(required = false) String requestBody){

        final ResponseTransfer<List<HorseRaceEventDTO>> responseTransfer;
        SortOption[] sortOptions=null;
        final JSONObject jsonObject = new JSONObject(requestBody);
        final boolean isActive=jsonObject.getBoolean("isActive");

        if(jsonObject.has("sort")){
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.getJSONArray("sort").toString(), SortOption[].class);
        }
        responseTransfer=horseRaceEventService.getHorseRaceEventDTOSBySearchKey(page, size, searchKey, sortOptions, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-event", produces = "application/json")
    public ResponseEntity<?>getEvent(@RequestParam("horseRaceEventUuid")UUID horseRaceEventUuid){

        final ResponseTransfer<HorseRaceEventDTO>responseTransfer=horseRaceEventService.getHorseRaceEventDTOByUuid(horseRaceEventUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-event", produces = "application/json")
    public ResponseEntity<?>editHorseRaceEvent(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID racetrackUuid=jsonParser.fromJson(jsonObject.getString("racetrackUuid"), UUID.class);
        final HorseRaceEvent horseRaceEvent=jsonParser.fromJson(jsonObject.getJSONObject("horseRaceEvent").toString(),
                HorseRaceEvent.class);

        responseTransfer=horseRaceEventService.editHorseRaceEvent(horseRaceEvent, racetrackUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-event", params = {"eventUuid"}, produces = "application/json")
    public ResponseEntity<?>removeHorseRaceEvent(@RequestParam("eventUuid")UUID horseRaceEventUuid){

        final ResponseTransfer<?>responseTransfer=horseRaceEventService.removeHorseRaceEventByUuid(horseRaceEventUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/event", produces = "application/json")
    public ResponseEntity<?>switchActivationHorseRaceEvent(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID horRaceEventUuid=UUID.fromString(jsonObject.getString("uuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=horseRaceEventService.switchActivationHorseRaceEventByUuid(horRaceEventUuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
