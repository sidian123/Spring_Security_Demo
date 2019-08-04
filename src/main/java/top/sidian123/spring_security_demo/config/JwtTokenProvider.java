package top.sidian123.spring_security_demo.config;

import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT帮助类
 * @author sidian
 * @date 2019/8/2 下午4:38
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * 填充用户数据到token中, 并返回
     * @param authentication
     * @return
     */
    public String generateToken(Authentication authentication) {

        //获取用户
        User user = (User) authentication.getPrincipal();


        //生成token
        return Jwts.builder()
                .claim("username",user.getUsername())
                .claim("roles",user.getAuthorities().stream()
                        .map(authority->{
                            return authority.getAuthority();
                        })
                        .collect(Collectors.toList())
                )
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }


    /**
     * 从token中获取用户信息
     * @param token
     * @return
     */
    public User getUserFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        User user=new User(
                claims.get("username",String.class),
                "just a placeholder",
                (Collection<? extends GrantedAuthority>) claims.get("roles", Collection.class).stream()
                .map(role->{
                  return new SimpleGrantedAuthority((String)role);
                })
                .collect(Collectors.toList())
        );

        return user;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
