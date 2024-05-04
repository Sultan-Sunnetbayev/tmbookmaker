package tm.salam.TmBookmaker.controllers.web;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.dtoes.models.CashRegisterDTO;
import tm.salam.TmBookmaker.dtoes.models.CityDTO;
import tm.salam.TmBookmaker.dtoes.models.PopulatedPlaceDTO;
import tm.salam.TmBookmaker.dtoes.models.RegionDTO;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.helpers.types.SortOption;
import tm.salam.TmBookmaker.models.CashRegister;
import tm.salam.TmBookmaker.services.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/settings/cash-register")
public class CashRegisterController {

    private final CashRegisterService cashRegisterService;
    private final RoleService roleService;
    private final RegionService regionService;
    private final CityService cityService;
    private final PopulatedPlaceService populatedPlaceService;
    private final JsonParser jsonParser;

    public CashRegisterController(CashRegisterService cashRegisterService, RoleService roleService,
                                  RegionService regionService, CityService cityService,
                                  PopulatedPlaceService populatedPlaceService, JsonParser jsonParser) {

        this.cashRegisterService = cashRegisterService;
        this.roleService = roleService;
        this.regionService = regionService;
        this.cityService = cityService;
        this.populatedPlaceService = populatedPlaceService;
        this.jsonParser = jsonParser;
    }

    @GetMapping(path = "/get-active/regions", produces = "application/json")
    public ResponseEntity<?>getActiveRegions(){

        final ResponseTransfer<List<RegionDTO>>responseTransfer= regionService.getActiveRegionDTOS();

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

    @GetMapping(path = "/get-active/populated-places", params = {"regionUuid", "cityUuid"}, produces = "application/json")
    public ResponseEntity<?>getActiveCities(@RequestParam(value = "regionUuid", required = false)UUID regionUuid,
                                            @RequestParam(value = "cityUuid", required = false)UUID cityUuid){

        final ResponseTransfer<List<PopulatedPlaceDTO>>responseTransfer=populatedPlaceService.getActivePopulatedPlaceDTOS(cityUuid, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/add-cash-register", produces = "application/json")
    public ResponseEntity<?>addCashRegister(@RequestBody String requestBody){

        final ResponseTransfer<UUID>responseTransferRoleService=roleService.getRoleUuidByName("ROLE_CASHIER");

        if(!responseTransferRoleService.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity.status(responseTransferRoleService.getHttpStatus())
                    .body(responseTransferRoleService.getResponseBody());
        }
        final ResponseTransfer<?> responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID populatedPlaceUuid=UUID.fromString(jsonObject.getString("populatedPlaceUuid"));
        final UUID cityUuid=UUID.fromString(jsonObject.getString("cityUuid"));
        final UUID regionUuid=UUID.fromString(jsonObject.getString("regionUuid"));
        final CashRegister cashRegister=jsonParser.fromJson(jsonObject.getJSONObject("cashRegister").toString(), CashRegister.class);

        assert cashRegister.getCashier()!=null;

        responseTransfer=cashRegisterService.addCashRegister(cashRegister, populatedPlaceUuid, cityUuid, regionUuid,
                responseTransferRoleService.getResponseBody().getData());

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-number/cash-registers", params = {"searchKey"}, produces = "application/json")
    public ResponseEntity<?>getNumberCashRegistersBySearchKey(@RequestParam("searchKey")String searchKey){

        final ResponseTransfer<Integer>responseTransfer=cashRegisterService.getNumberCashRegistersBySearchKey(searchKey);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/get-cash-registers", params = {"page", "size", "searchKey"}, produces = "application/json")
    public ResponseEntity<?>getCashRegistersBySearchKey(@RequestParam(value = "page", defaultValue = "0")int page,
                                                        @RequestParam(value = "size", defaultValue = "10")int size,
                                                        @RequestParam(value = "searchKey", defaultValue = "")String searchKey,
                                                        @RequestBody(required = false)String requestBody){

        final ResponseTransfer<List<CashRegisterDTO>>responseTransfer;
        SortOption[] sortOptions=null;

        if(requestBody!=null) {
            final JSONObject jsonObject = new JSONObject(requestBody);
            sortOptions = jsonParser.fromJsonAsArray(jsonObject.get("sort").toString(), SortOption[].class);
        }
        responseTransfer=cashRegisterService.getCashRegisterDTOSBySearchKey(page, size, searchKey, sortOptions);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @GetMapping(path = "/get-cash-register", params = {"cashRegisterUuid"}, produces = "application/json")
    public ResponseEntity<?>getCashRegister(@RequestParam("cashRegisterUuid")UUID cashRegisterUuid){

        final ResponseTransfer<CashRegisterDTO>responseTransfer=cashRegisterService.getCashRegisterDTOByUuid(cashRegisterUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/edit-cash-register", produces = "application/json")
    public ResponseEntity<?>editCashRegister(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID populatedPlaceUuid=UUID.fromString(jsonObject.getString("populatedPlaceUuid"));
        final UUID cityUuid=UUID.fromString(jsonObject.getString("cityUuid"));
        final UUID regionUuid=UUID.fromString(jsonObject.getString("regionUuid"));
        final CashRegister cashRegister=jsonParser.fromJson(jsonObject.getJSONObject("cashRegister").toString(), CashRegister.class);

        responseTransfer=cashRegisterService.editCashRegister(cashRegister, populatedPlaceUuid, cityUuid, regionUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/remove-cash-register", params = {"cashRegisterUuid"}, produces = "application/json")
    public ResponseEntity<?>removeCashRegister(@RequestParam("cashRegisterUuid")UUID cashRegisterUuid){

        final ResponseTransfer<?>responseTransfer=cashRegisterService.markCashRegisterAsRemoved(cashRegisterUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/switch-activation/cash-register", produces = "application/json")
    public ResponseEntity<?>switchActivationCashRegister(@RequestBody String requestBody){

        final ResponseTransfer<?>responseTransfer;
        final JSONObject jsonObject=new JSONObject(requestBody);
        final UUID uuid=UUID.fromString(jsonObject.getString("uuid"));
        final boolean isActive=jsonObject.getBoolean("isActive");

        responseTransfer=cashRegisterService.switchActivationCashRegisterByUuid(uuid, isActive);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
