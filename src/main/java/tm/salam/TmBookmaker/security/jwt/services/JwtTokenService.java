package tm.salam.TmBookmaker.security.jwt.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tm.salam.TmBookmaker.helpers.JsonParser;
import tm.salam.TmBookmaker.security.jwt.models.JwtUserDetails;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {

    private final String SECRET_KEY="2aa9bd556374704174bf891976d3098786acad1eac9397cb5ea8704a376a6d48";
    private final JsonParser jsonParser;

    public JwtTokenService(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public String extractLoginFromToken(final String token){

        return extractClaimWithFunction(token, Claims::getSubject);
    }

    public <T> T extractClaimWithFunction(final String token, Function<Claims, T> claimsResolver){

        final Claims claims=extractAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    public <T> T extractClaimWithKey(final String token, final String key, Class<T> tClass) {

        final Claims claims=extractAllClaimsFromToken(token);
        final String claimStrValue=claims.get(key).toString();

        return jsonParser.fromJson(claimStrValue, tClass);
    }

    public Claims extractAllClaimsFromToken(final String token){

        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){

        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(final JwtUserDetails jwtUserDetails){

        return generateToken(new HashMap<>(), jwtUserDetails);
    }

    public String generateToken(final Map<String, Object>extraClaims, final UserDetails userDetails){

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofHours(24).toMillis()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(final String token, final JwtUserDetails jwtUserDetails){

        final String username=extractLoginFromToken(token);

        return jwtUserDetails.getUsername().equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token){

        return extractExpirationFromToken(token).before(new Date());
    }

    private Date extractExpirationFromToken(final String token){

        return extractClaimWithFunction(token, Claims::getExpiration);
    }

}
