package tm.salam.TmBookmaker.controllers.web;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.CityDTO;
import tm.salam.TmBookmaker.dtoes.models.RacetrackDTO;
import tm.salam.TmBookmaker.dtoes.models.RegionDTO;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.Racetrack;
import tm.salam.TmBookmaker.services.CityService;
import tm.salam.TmBookmaker.services.RacetrackService;
import tm.salam.TmBookmaker.services.RegionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/settings/racetrack")
public class RacetrackController {

    private final RacetrackService racetrackService;
    private final RegionService regionService;
    private final CityService cityService;
    private final JsonParser jsonParser;

    public RacetrackController(RacetrackService racetrackService, RegionService regionService, CityService cityService,
                               JsonParser jsonParser) {
        this.racetrackService = racetrackService;
        this.regionService = regionService;
        this.cityService = cityService;
        this.jsonParser = jsonParser;
    }

    @GetMapping(path = "/get-active/regions", produces = "application/json")
    public ResponseEntity<?>getRegions(){

        final ResponseTransfer<List<RegionDTO>> responseTransfer=regionService.getActiveRegionDTOS();

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-active/cities", params = {"regionUuid"}, produces = "application/json")
    public ResponseEntity<?>getActiveCities(@RequestParam(value = "regionUuid", required = false)UUID regionUuid){

        final ResponseTransfer<List<CityDTO>> responseTransfer;

        if(regionUuid!=null) {
            responseTransfer=cityService.getActiveCityDTOSByRegionUuid(regionUuid);
        }else{
            responseTransfer=cityService.getActiveCityDTOS();
        }

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-racetrack", produces = "application/json")
    public ResponseEntity<?>addRacetrack(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final Racetrack racetrack=jsonParser.fromJson(jsonObject.getJSONObject("racetrack").toString(),
                Racetrack.class);
        final UUID cityUuid=jsonParser.fromJson(jsonObject.getString("cityUuid"), UUID.class);
        final UUID regionUuid=jsonParser.fromJson(jsonObject.getString("regionUuid"), UUID.class);

        responseTransfer=racetrackService.addRacetrack(racetrack, cityUuid, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/number-racetracks", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>getNumberRacetracks(@RequestParam(value = "searchKey", defaultValue = "")String searchKey){

        final ResponseTransfer<Integer>responseTransfer=racetrackService.getNumberRacetracksBySearchKey(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-racetracks", params = {"page", "size", "searchKey"}, produces = "application/json")
    public ResponseEntity<?>getRacetracks(@RequestParam(value = "page", defaultValue = "0")int page,
                                          @RequestParam(value = "size", defaultValue = "10")int size,
                                          @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                          @RequestBody(required = false)String requestBody){

        final ResponseTransfer<List<RacetrackDTO>> responseTransfer;
        SortOption[] sortOptions=null;

        if(requestBody!=null) {
            final JSONObject jsonObject = new JSONObject(requestBody);
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.get("sort").toString(), SortOption[].class);
        }
        responseTransfer=racetrackService.getRacetrackDTOSBySearchKey(page, size, searchKey, sortOptions);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-racetrack", params = {"racetrackUuid"}, produces = "application/json")
    public ResponseEntity<?>getRacetrack(@RequestParam("racetrackUuid")UUID racetrackUuid){

        final ResponseTransfer<RacetrackDTO>responseTransfer=racetrackService.getRacetrackDTOByUuid(racetrackUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-racetrack", produces = "application/json")
    public ResponseEntity<?>editRacetrack(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final Racetrack racetrack=jsonParser.fromJson(jsonObject.getJSONObject("racetrack").toString(), Racetrack.class);
        final UUID cityUuid=jsonParser.fromJson(jsonObject.getString("cityUuid"), UUID.class);
        final UUID regionUuid=jsonParser.fromJson(jsonObject.getString("regionUuid"), UUID.class);

        responseTransfer=racetrackService.editRacetrack(racetrack, cityUuid, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-racetrack", params = {"racetrackUuid"}, produces = "application/json")
    public ResponseEntity<?>addRacetrack(@RequestParam("racetrackUuid")UUID racetrackUuid){

        final ResponseTransfer<?>responseTransfer=racetrackService.removeRacetrackByUuid(racetrackUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/racetrack", produces = "application/json")
    public ResponseEntity<?>switchActivationRacetrack(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID racetrackUuid=UUID.fromString(jsonObject.getString("uuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=racetrackService.switchActivationRacetrackByUuid(racetrackUuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
