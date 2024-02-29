package com.bezina.myNotes.security;

import com.bezina.myNotes.DAO.UserRepository;
import com.bezina.myNotes.entities.User;
import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JWTTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);
    @Autowired
    UserRepository userRepo;
    public String getToken(Authentication authentication){
        LOG.info("get Token");
        LOG.info(authentication.getPrincipal().toString());
      //  User user = (User) authentication.getPrincipal();
        Optional<User> optionalUser = userRepo.findUserByEmail(authentication.getPrincipal().toString());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();

        Date now = new Date(System.currentTimeMillis());
        Date expiringDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());
           // Jwts.builder().

        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiringDate)
            //    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .signWith(SecurityConstants.SECRET_KEY, SignatureAlgorithm.HS256) // Устанавливаем подпись
                //    .signWith(, SecurityConstants.SECRET) -- depricated
                .compact();}
        else {LOG.error("no such user {}"+ authentication.getPrincipal().toString());
            return null;}
    }

    public boolean validateToken(String token){
        try {

       /*     Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);*/
          /*  Jwts.parserBuilder()
                    .setSigningKey(SecurityConstants.SECRET_KEY)
                    .build()
                    .parseClaimsJwt(token);
*/
            return true;
        }catch (SignatureException|
                MalformedJwtException|
                ExpiredJwtException |
                UnsupportedJwtException|
                IllegalArgumentException ex)
        {
            LOG.error(ex.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token){
    /*    Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        -- depricated
      */

// Используйте JwtParserBuilder для создания парсера JWT
       JwtParser parser = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET_KEY)
                .build();

// Парсим токен с помощью созданного парсера
        Jws<Claims> jws = parser.parseClaimsJws(token);

// Получаем тело (payload) токена
        Claims claims = jws.getBody();

        String getId = (String) claims.get("id");
        return Long.parseLong(getId);
     //   return null;
    }

}
