package am.nova.user.management.demo.util;

import am.nova.user.management.demo.model.AuthenticatedUser;
import am.nova.user.management.demo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

       private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    @Value("${secret}")
    String secret;

    @Value("${expiration}")
    private Long expiration;

    @Autowired
    private UserService userService;

    public String getUsernameFromToken(String token) { return getClaimFromToken(token, Claims::getSubject); }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) { return getClaimFromToken(token, Claims::getExpiration); }

    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }
    public String generateToken(AuthenticatedUser jwtUser, Device device) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", jwtUser.getUserId());
        claims.put("email", jwtUser.getUsername());
        claims.put("password", jwtUser.getPassword());
        claims.put("authorities", jwtUser.getAuthorities());
        claims.put("createdAt", jwtUser.getLastPasswordResetDate());
        claims.put("enabled", jwtUser.isEnabled());
        return doGenerateAccessToken(claims, jwtUser.getUsername(), generateAudience(device));
    }


    public AuthenticatedUser getAllUserDataFromToken(String token){
        final Claims claims = getAllClaimsFromToken(token);

        AuthenticatedUser jwtUser = new AuthenticatedUser();
        jwtUser.setUserId(UUID.fromString((String)claims.get("user_id")));
        jwtUser.setUserName((String)claims.get("userName"));
        jwtUser.setPassword(((String) claims.get("password")));
        ArrayList<Object> objects = (ArrayList<Object>) claims.get("authorities");
        LinkedHashMap<String, String> linkedHashMap = null;

        Set<String> authorities = new HashSet<>();

        //List<Authority> authorities = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            linkedHashMap= (LinkedHashMap<String, String>) objects.get(i);
            List<String> keys = new ArrayList<>(linkedHashMap.keySet());
            for (String key :
                    keys) {
                authorities.add(linkedHashMap.get(key));
            }
        }
        jwtUser.setAuthorities(authorities);

        return  jwtUser;
    }



    public UUID getIdFromToken(String token){
        final Claims claims = getAllClaimsFromToken(token);
        UUID uuid = UUID.fromString((String)claims.get("user_id"));
        return uuid;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return body;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }



    private String doGenerateAccessToken(Map<String, Object> claims, String subject, String audience) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDateForAccesToken(createdDate);

        System.out.println("doGenerateToken " + createdDate);
        System.out.println(expirationDate);

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .compact();
    }
    public String accessTokenfromRefreshToken(String token, Device device) {
        /*final Date createdDate = timeProvider.now();
        final Date expirationDate = calculateExpirationDateForAccesToken(createdDate);*/

        UUID id = getIdFromToken(token);
        AuthenticatedUser jwtUser = AuthenticatedUserFactory.create(userService.getUser(id));

        return generateToken(jwtUser, device);
        /*JwtUser jwtUser = getAllUserDataFromToken(token);
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", jwtUser.getId());
        claims.put("firsName", jwtUser.getFirstname());
        claims.put("lastName", jwtUser.getLastname());
        claims.put("email", jwtUser.getUsername());
        claims.put("password", jwtUser.getPassword());
        claims.put("authorities", jwtUser.getAuthorities());
        claims.put("createdAt", jwtUser.getLastPasswordResetDate());
        claims.put("enabled", jwtUser.getEnabled());*/
        /*return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject(jwtUser.getUsername())
                .setAudience(generateAudience(device))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .compact();*/
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String generateRefreshToken(AuthenticatedUser jwtUser, Device device) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDateForRefreshToken(createdDate);

        Map<String, Object> claims = new HashMap<>();

        claims.put("user_id", jwtUser.getUserId());
        claims.put("userName", jwtUser.getUsername());
        claims.put("password", jwtUser.getPassword());

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setClaims(claims)
                .setSubject(jwtUser.getUsername())
                .setAudience(generateAudience(device))
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .compact();
    }



    public Boolean validateToken(String token, UserDetails userDetails) {
        AuthenticatedUser user = (AuthenticatedUser) userDetails;
        final String username = getUsernameFromToken(token);
        //final Date created = getIssuedAtDateFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(expiration, user.getLastPasswordResetDate())
        );
    }

    private Date calculateExpirationDateForAccesToken(Date createdDate) {
        return new Date(createdDate.getTime() + expiration*12);
    }

    private Date calculateExpirationDateForRefreshToken(Date createdDate){
        return new Date(createdDate.getTime() + expiration*25920);
    }
}
