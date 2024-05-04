package tm.salam.TmBookmaker.controllers.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.CityDTO;
import tm.salam.TmBookmaker.dtoes.models.PopulatedPlaceDTO;
import tm.salam.TmBookmaker.dtoes.models.RegionDTO;
import tm.salam.TmBookmaker.helpers.*;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.City;
import tm.salam.TmBookmaker.models.PopulatedPlace;
import tm.salam.TmBookmaker.models.Region;
import tm.salam.TmBookmaker.services.CityService;
import tm.salam.TmBookmaker.services.PopulatedPlaceService;
import tm.salam.TmBookmaker.services.RegionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/settings/location")
public class LocalizationController {

    private final RegionService regionService;
    private final CityService cityService;
    private final PopulatedPlaceService populatedPlaceService;
    private final JsonParser jsonParser;

    public LocalizationController(RegionService regionService, CityService cityService,
                                  PopulatedPlaceService populatedPlaceService, JsonParser jsonParser) {
        this.regionService = regionService;
        this.cityService = cityService;
        this.populatedPlaceService = populatedPlaceService;
        this.jsonParser = jsonParser;
    }

    @PostMapping(path = "/add-region", produces = "application/json")
    public ResponseEntity<?> addRegion(@RequestBody @Validated Region region){

        final ResponseTransfer<?> responseTransfer= regionService.addRegion(region);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/number-regions", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>getNumberRegionsBySearchKey(@RequestParam(value = "searchKey", defaultValue = "")String searchKey){

        final ResponseTransfer<Integer> responseTransfer= regionService.getNumberRegionsBySearchKey(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-regions", params = {"page", "size", "searchKey"}, produces="application/json")
    public ResponseEntity<?>getRegionsBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                  @RequestParam(value = "size", defaultValue = "10")int size,
                                                  @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                                  @RequestBody(required = false) String requestBody) {

        final ResponseTransfer<List<RegionDTO>> responseTransfer;
        SortOption[] sortOptions=null;

        if(requestBody!=null) {
            final JSONObject jsonObject = new JSONObject(requestBody);
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.get("sort").toString(), SortOption[].class);
        }

        responseTransfer = regionService.getRegionDTOSBySearchKey(searchKey, page, size, sortOptions);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-region", params = {"regionUuid"}, produces = "application/json")
    public ResponseEntity<?>getRegion(@RequestParam("regionUuid")UUID regionUuid){

        final ResponseTransfer<RegionDTO>responseTransfer= regionService.getRegionDTOByUuid(regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-region", produces = "application/json")
    public ResponseEntity<?>editRegion(@RequestBody @Validated Region region){

        final ResponseTransfer<?>responseTransfer= regionService.editRegion(region);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-region", params = {"regionUuid"}, produces = "application/json")
    public ResponseEntity<?>removeRegion(@RequestParam("regionUuid")UUID regionUuid){

        final ResponseTransfer<?>responseTransfer= regionService.removeRegionByUuid(regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/region", produces = "application/json")
    public ResponseEntity<?>switchActivationRegion(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID regionUuid=UUID.fromString(jsonObject.getString("uuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=regionService.switchActivationRegionByUuid(regionUuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-active/regions", produces = "application/json")
    public ResponseEntity<?>getActiveRegions(){

        final ResponseTransfer<List<RegionDTO>>responseTransfer= regionService.getActiveRegionDTOS();

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-city", produces = "application/json")
    public ResponseEntity<?>addCity(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final City city;
        final UUID regionUuid;
        try {
            JSONObject jsonObject=new JSONObject(requestBody);
            ObjectMapper cityMapper=new ObjectMapper();
            city=cityMapper.readValue(jsonObject.getJSONObject("city").toString(), City.class);
            regionUuid=UUID.fromString(jsonObject.getString("regionUuid"));
        }catch(JSONException | JsonProcessingException exception){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .responseBody(ResponseBody.builder()
                            .message(exception.getMessage())
                            .build())
                    .build();

            return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
        }
        responseTransfer=cityService.addCity(city, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/number-cities", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>getNumberCities(@RequestParam("searchKey")String searchKey){

        final ResponseTransfer<Integer>responseTransfer=cityService.getNumberCitiesBySearchKey(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-cities", params = {"page", "size", "searchKey"}, produces = "application/json")
    public ResponseEntity<?>getCitiesBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                 @RequestParam(value = "size", defaultValue = "10")int size,
                                                 @RequestParam(value = "searchKey",defaultValue = "")String searchKey,
                                                 @RequestBody(required = false)String requestBody) {

        final ResponseTransfer<?>responseTransfer;
        SortOption[] sortOptions=null;

        if(requestBody!=null) {
            final JSONObject jsonObject = new JSONObject(requestBody);
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.get("sort").toString(), SortOption[].class);
        }

        responseTransfer=cityService.getCityDTOSBySearchKey(searchKey, page, size, sortOptions);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-city", params = {"cityUuid"}, produces = "application/json")
    public ResponseEntity<?>getCity(@RequestParam("cityUuid")UUID cityUuid){

        final ResponseTransfer<?>responseTransfer=cityService.getCityDTOByUuid(cityUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-city", produces = "application/json")
    public ResponseEntity<?>editCity(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final City city=jsonParser.fromJson(jsonObject.getJSONObject("city").toString(), City.class);
        final UUID regionUuid=UUID.fromString(jsonObject.getString("regionUuid"));

        responseTransfer=cityService.editCity(city, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-city", params = {"cityUuid"}, produces = "application/json")
    public ResponseEntity<?>removeCity(@RequestParam("cityUuid")UUID cityUuid){

        final ResponseTransfer<?>responseTransfer=cityService.removeCityByUuid(cityUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/city", produces = "application/json")
    public ResponseEntity<?>switchActivationCity(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID cityUuid=UUID.fromString(jsonObject.getString("uuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=cityService.switchActivationCityByUuid(cityUuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-active/cities", params = {"regionUuid"}, produces = "application/json")
    public ResponseEntity<?>getActiveCities(@RequestParam(value = "regionUuid", required = false)UUID regionUuid){

        final ResponseTransfer<List<CityDTO>>responseTransfer;

        if(regionUuid==null){
            responseTransfer=cityService.getActiveCityDTOS();
        }else{
            responseTransfer=cityService.getActiveCityDTOSByRegionUuid(regionUuid);
        }

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-populated-place", produces = "application/json")
    public ResponseEntity<?>addPopulatedPlace(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID regionUuid=UUID.fromString(jsonObject.getString("regionUuid"));
        final UUID cityUuid=UUID.fromString(jsonObject.getString("cityUuid"));
        final PopulatedPlace populatedPlace=jsonParser.fromJson(jsonObject.getJSONObject("populatedPlace").toString(),
                PopulatedPlace.class);

        responseTransfer= populatedPlaceService.addPopulatedPlace(populatedPlace, cityUuid, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-number/populated-places", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>getNumberPopulatedPlacesBySearchKey(@RequestParam(value = "searchKey", defaultValue = "")String searchKey){

        final ResponseTransfer<Integer>responseTransfer= populatedPlaceService.getNumberPopulatedPlacesBySearchKey(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-populated-places", params = {"page", "size", "searchKey"}, produces = "application/json")
    public ResponseEntity<?>getPopulatedPlacesBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                          @RequestParam(value = "size", defaultValue = "10")int size,
                                                          @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                                          @RequestBody(required = false)String requestBody){
        final ResponseTransfer<List<PopulatedPlaceDTO>> responseTransfer;
        SortOption[] sortOptions=null;

        if(requestBody!=null) {
            final JSONObject jsonObject = new JSONObject(requestBody);
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.get("sort").toString(), SortOption[].class);
        }
        responseTransfer= populatedPlaceService.getPopulatedPlaceDTOSBySearchKey(page, size, searchKey, sortOptions);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-populated-place", params = {"populatedPlaceUuid"}, produces = "application/json")
    public ResponseEntity<?>getPopulatedPlace(@RequestParam("populatedPlaceUuid")UUID populatedPlaceUuid){

        final ResponseTransfer<PopulatedPlaceDTO>responseTransfer=populatedPlaceService.getPopulatedPlaceDTOByUuid(populatedPlaceUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-populated-place", produces = "application/json")
    public ResponseEntity<?>editPopulatedPlace(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID regionUuid=UUID.fromString(jsonObject.getString("regionUuid"));
        final UUID cityUuid=UUID.fromString(jsonObject.getString("cityUuid"));
        final PopulatedPlace populatedPlace=jsonParser.fromJson(jsonObject.getJSONObject("populatedPlace").toString(),
                PopulatedPlace.class);

        responseTransfer=populatedPlaceService.editPopulatedPlace(populatedPlace, cityUuid, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-populated-place", params = {"populatedPlaceUuid"}, produces = "application/json")
    public ResponseEntity<?>removePopulatedPlace(@RequestParam("populatedPlaceUuid")UUID populatedPlaceUuid){

        final ResponseTransfer<?>responseTransfer=populatedPlaceService.removePopulatedPlaceByUuid(populatedPlaceUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/populated-place", produces = "application/json")
    public ResponseEntity<?>switchActivationPopulatedPlace(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID uuid=UUID.fromString(jsonObject.getString("uuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=populatedPlaceService.switchActivationPopulatedPlaceByUuid(uuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
