package tm.salam.TmBookmaker.controllers.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.salam.TmBookmaker.dtoes.models.BettorDTO;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Bettor;
import tm.salam.TmBookmaker.services.AuthenticationService;
import tm.salam.TmBookmaker.services.BettorService;
import tm.salam.TmBookmaker.services.RoleService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/app/public")
public class AppPublicController {

    private final AuthenticationService authenticationService;
    private final BettorService bettorService;
    private final RoleService roleService;

    private final String roleName="ROLE_BETTOR";

    public AppPublicController(AuthenticationService authenticationService, BettorService bettorService, RoleService roleService) {
        this.authenticationService = authenticationService;
        this.bettorService = bettorService;
        this.roleService = roleService;
    }

    @PostMapping(path = "/registration-bettor", produces = "application/json")
    public ResponseEntity<?> addBettor(@RequestBody Bettor bettor){

        final ResponseTransfer<UUID> responseTransferRole=roleService.getRoleUuidByName(roleName);

        if(!responseTransferRole.getHttpStatus().is2xxSuccessful()){

            return ResponseEntity.status(responseTransferRole.getHttpStatus()).body(responseTransferRole.getResponseBody());
        }
        ResponseTransfer<?>responseTransfer=bettorService.registrateBettor(bettor, responseTransferRole.getResponseBody().getData());

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

    @PostMapping(path = "/confirm-bettor", produces = "application/json")
    public ResponseEntity<?>confirmBettor(@RequestBody Bettor bettor){

        final ResponseTransfer<UUID>responseTransferConfirmation=bettorService.confirmBettorByActivationCode(bettor);
        if(!responseTransferConfirmation.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity.status(responseTransferConfirmation.getHttpStatus())
                    .body(responseTransferConfirmation.getResponseBody());
        }
        final ResponseTransfer<BettorDTO>responseTransferProfile=bettorService.getBettorDTOProfileByUuidForAppClient(
                responseTransferConfirmation.getResponseBody().getData());

        return ResponseEntity.status(responseTransferProfile.getHttpStatus()).body(responseTransferProfile.getResponseBody());
    }

    @PostMapping(path = "/activate-bettor", produces = "application/json")
    public ResponseEntity<?>activateBettor(@RequestBody Bettor bettor){

        ResponseTransfer<?>responseTransferBettorService=bettorService.activateBettor(bettor);

        if(!responseTransferBettorService.getHttpStatus().is2xxSuccessful()){
            return ResponseEntity.status(responseTransferBettorService.getHttpStatus()).body(responseTransferBettorService.getResponseBody());
        }
        ResponseTransfer<?>responseTransferAuthenticationService=authenticationService.authenticateBettor(bettor);

        return ResponseEntity.status(responseTransferBettorService.getHttpStatus()).body(responseTransferAuthenticationService.getResponseBody());
    }

    @PostMapping(path = "/authenticate-bettor", produces = "application/json")
    public ResponseEntity<?>authenticateBettor(@RequestBody Bettor bettor){

        final ResponseTransfer<?>responseTransfer=authenticationService.authenticateBettor(bettor);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
