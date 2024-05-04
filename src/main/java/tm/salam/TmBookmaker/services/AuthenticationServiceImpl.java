package tm.salam.TmBookmaker.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tm.salam.TmBookmaker.daoes.BettorRepository;
import tm.salam.TmBookmaker.daoes.UserRepository;
import tm.salam.TmBookmaker.helpers.ResponseBody;
import tm.salam.TmBookmaker.helpers.ResponseTransfer;
import tm.salam.TmBookmaker.models.Bettor;
import tm.salam.TmBookmaker.models.User;
import tm.salam.TmBookmaker.security.jwt.serializers.JwtUserDetailsSerializer;
import tm.salam.TmBookmaker.security.jwt.services.JwtTokenService;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BettorRepository bettorRepository;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsSerializer jwtUserDetailsSerializer;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, BettorRepository bettorRepository,
                                     JwtTokenService jwtTokenService, AuthenticationManager authenticationManager,
                                     JwtUserDetailsSerializer jwtUserDetailsSerializer, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bettorRepository = bettorRepository;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsSerializer = jwtUserDetailsSerializer;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseTransfer<?> authenticateUser(final User user){

        final ResponseTransfer<?> responseTransfer;
        final User authenticatedUser=userRepository.getUserByLogin(user.getLogin());

        if(authenticatedUser==null){
            throw new UsernameNotFoundException("error user login uncorrect");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
        );
        final Map<String, Object>data=new HashMap<>();

        data.put("uuid", authenticatedUser.getUuid());
        data.put("role", authenticatedUser.getRole().getName());
        final String token=jwtTokenService.generateToken(data, jwtUserDetailsSerializer.convertUsertoJwtUserDetails(authenticatedUser));

        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.builder()
                        .message("accept user successful authenticated")
                        .data(token)
                        .build())
                .build();

        return responseTransfer;
    }

    @Override
    public ResponseTransfer<?> authenticateBettor(final Bettor bettor) {

        final ResponseTransfer<?> responseTransfer;
        final Bettor authenticateBettor=bettorRepository.getBettorByPhoneNumber(bettor.getPhoneNumber());
        if(authenticateBettor==null){
            throw new UsernameNotFoundException("error user login uncorrect");
        }
        if(!bettor.getUsername().equals(authenticateBettor.getUsername()) ||
                !passwordEncoder.matches(bettor.getPhoneNumber(), authenticateBettor.getPassword())){
            responseTransfer=ResponseTransfer.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .responseBody(ResponseBody.builder()
                            .message("error bettor don't authenticated")
                            .build())
                    .build();

            return responseTransfer;
        }
        final Map<String, Object>data=new HashMap<>();

        data.put("uuid", authenticateBettor.getUuid());
        data.put("role", authenticateBettor.getRole().getName());
        final String token=jwtTokenService.generateToken(data,
                jwtUserDetailsSerializer.convertBettortoJwtUserDetails(authenticateBettor));

        responseTransfer=ResponseTransfer.builder()
                .httpStatus(HttpStatus.OK)
                .responseBody(ResponseBody.builder()
                        .message("accept bettor successful authenticated")
                        .data(token)
                        .build())
                .build();

        return responseTransfer;
    }

}
