package tm.salam.TmBookmaker.controllers.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.User;
import tm.salam.TmBookmaker.services.AuthenticationService;

@RestController
@RequestMapping("/api/v1/web/public")
public class WebPublicController {

    private final AuthenticationService authenticationService;

    public WebPublicController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<?>login(@RequestBody User user){

        final ResponseTransfer<?> responseTransfer=authenticationService.authenticateUser(user);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
