package tm.salam.TmBookmaker.security.jwt.services;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tm.salam.TmBookmaker.daoes.BettorRepository;
import tm.salam.TmBookmaker.daoes.UserRepository;
import tm.salam.TmBookmaker.models.Bettor;
import tm.salam.TmBookmaker.models.User;
import tm.salam.TmBookmaker.security.jwt.models.JwtUserDetails;
import tm.salam.TmBookmaker.security.jwt.serializers.JwtUserDetailsSerializer;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

    private final UserRepository userRepository;
    private final BettorRepository bettorRepository;
    private final JwtUserDetailsSerializer jwtUserDetailsSerializer;

    public JwtUserDetailsServiceImpl(UserRepository userRepository, BettorRepository bettorRepository,
                                     JwtUserDetailsSerializer jwtUserDetailsSerializer){
        this.userRepository = userRepository;
        this.bettorRepository = bettorRepository;
        this.jwtUserDetailsSerializer = jwtUserDetailsSerializer;
    }

    @Override
    public JwtUserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        final User user =userRepository.getUserByLogin(username);

        if(user==null){
            throw new UsernameNotFoundException("error user not found with this login");
        }

        return jwtUserDetailsSerializer.convertUsertoJwtUserDetails(user);
    }

    @Override
    public JwtUserDetails loadBettorByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {

        final Bettor bettor=bettorRepository.getBettorByPhoneNumber(phoneNumber);

        if(bettor==null){
            throw new UsernameNotFoundException("error bettor not found with this phone number");
        }

        return jwtUserDetailsSerializer.convertBettortoJwtUserDetails(bettor);
    }

}
