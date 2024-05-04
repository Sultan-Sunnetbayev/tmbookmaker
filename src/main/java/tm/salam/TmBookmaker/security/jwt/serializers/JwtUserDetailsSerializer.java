package tm.salam.TmBookmaker.security.jwt.serializers;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import tm.salam.TmBookmaker.models.Bettor;
import tm.salam.TmBookmaker.models.User;
import tm.salam.TmBookmaker.security.jwt.models.JwtUserDetails;

import java.util.Collection;
import java.util.List;

@Component
public class JwtUserDetailsSerializer {

    public JwtUserDetails convertUsertoJwtUserDetails(@NonNull final User user){

        return JwtUserDetails.builder()
                .uuid(user.getUuid())
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(strRoleToGrantedAuthority(user.getRole().getName()))
                .isActive(user.isActive())
                .build();
    }

    public JwtUserDetails convertBettortoJwtUserDetails(@NonNull final Bettor bettor){

        return JwtUserDetails.builder()
                .uuid(bettor.getUuid())
                .username(bettor.getPhoneNumber())
                .password(bettor.getPassword())
                .authorities(strRoleToGrantedAuthority(bettor.getRole().getName()))
                .isActive(bettor.isActive())
                .build();
    }

    private Collection<GrantedAuthority>strRoleToGrantedAuthority(@NonNull final String role){

        return List.of(new SimpleGrantedAuthority(role));
    }

}
