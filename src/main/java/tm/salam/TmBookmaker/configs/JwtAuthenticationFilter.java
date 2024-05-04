package tm.salam.TmBookmaker.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tm.salam.TmBookmaker.security.jwt.models.JwtUserDetails;
import tm.salam.TmBookmaker.security.jwt.services.JwtTokenService;
import tm.salam.TmBookmaker.security.jwt.services.JwtUserDetailsService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final JwtUserDetailsService jwtUserDetailsService;


    public JwtAuthenticationFilter(JwtTokenService jwtTokenService, JwtUserDetailsService jwtUserDetailsService){
        this.jwtTokenService = jwtTokenService;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader=request.getHeader("Authorization");

        if(authorizationHeader==null || !authorizationHeader.startsWith("Bearer_")){
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken=authorizationHeader.substring(7);
        final String login = jwtTokenService.extractLoginFromToken(jwtToken);

        if (login != null && SecurityContextHolder.getContext().getAuthentication()==null) {
            final JwtUserDetails jwtUserDetails;
            if(request.getRequestURI().startsWith("/api/v1/web/")){
                jwtUserDetails=jwtUserDetailsService.loadUserByUsername(login);
            }else{
                jwtUserDetails=jwtUserDetailsService.loadBettorByPhoneNumber(login);
            }
            if(jwtTokenService.isTokenValid(jwtToken, jwtUserDetails)) {
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        jwtUserDetails, "", jwtUserDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
