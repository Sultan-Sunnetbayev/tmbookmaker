package tm.salam.TmBookmaker.controllers.app;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.HorseRaceEventDTO;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.services.HorseRaceEventService;
import tm.salam.TmBookmaker.services.HorseRaceService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/app/result")
public class ResultController {

    private final HorseRaceEventService horseRaceEventService;
    private final HorseRaceService horseRaceService;
    private final JsonParser jsonParser;

    public ResultController(HorseRaceEventService horseRaceEventService, HorseRaceService horseRaceService,
                            JsonParser jsonParser) {
        this.horseRaceEventService = horseRaceEventService;
        this.horseRaceService = horseRaceService;
        this.jsonParser = jsonParser;
    }

    @PostMapping(path = "/get-horse-race-events", produces = "application/json")
    public ResponseEntity<?> getHorseRaceEvents(@RequestBody(required = false) String requestBody){

        ResponseTransfer<List<HorseRaceEventDTO>>responseTransfer;
        final JSONObject jsonObject;
        final LocalDate horseRaceEventDate;
        if(requestBody!=null){
            jsonObject=new JSONObject(requestBody);
            horseRaceEventDate=jsonParser.fromJson(jsonObject.getString("horseRaceEventDate"), LocalDate.class);
            responseTransfer=horseRaceEventService.getHorseRaceEventDTOSByDate(horseRaceEventDate);
        }else{
            responseTransfer=horseRaceEventService.getLastHorseRaceEventDTOS();
        }

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-results/horse-races", produces = "application/json")
    public ResponseEntity<?>getResultsHorseRaces(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID bettorUuid=UUID.fromString(jsonObject.getString("bettorUuid"));
        final UUID horseRaceEventUuid=UUID.fromString(jsonObject.getString("horseRaceEventUuid"));

        responseTransfer=horseRaceService.getResultHorseRaceDTOSByHorseRaceEventUuidAndBettorUuid(horseRaceEventUuid, bettorUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
