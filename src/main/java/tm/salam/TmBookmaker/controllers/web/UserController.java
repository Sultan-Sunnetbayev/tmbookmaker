package tm.salam.TmBookmaker.controllers.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.security.jwt.services.JwtTokenService;
import tm.salam.TmBookmaker.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/web/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    public UserController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping(path = "/get-profile", produces = "application/json")
    public ResponseEntity<?>getProfile(@RequestHeader("Authorization")String authorizationToken){

        final ResponseTransfer<?> responseTransfer;
        final UUID userUuid=jwtTokenService.extractClaimWithKey(authorizationToken.substring(7), "uuid", UUID.class);

        responseTransfer=userService.getUserDTOByUuid(userUuid);

        return ResponseEntity.status(responseTransfer.getHttpStatus()).body(responseTransfer.getResponseBody());
    }

}
