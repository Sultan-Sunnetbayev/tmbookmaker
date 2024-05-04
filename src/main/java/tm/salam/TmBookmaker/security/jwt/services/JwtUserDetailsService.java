package tm.salam.TmBookmaker.security.jwt.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tm.salam.TmBookmaker.security.jwt.models.JwtUserDetails;

public interface JwtUserDetailsService extends UserDetailsService  {

    @Override
    JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    JwtUserDetails loadBettorByPhoneNumber(String phoneNumber) throws UsernameNotFoundException;

}
