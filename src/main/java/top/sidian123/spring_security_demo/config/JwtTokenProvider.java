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

import java.time.LocalDateTime;
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



    /**
     * 填充用户数据到token中, 并返回
     * @param authentication
     * @return
     */
    public String generateToken(Authentication authentication) {

        //获取用户
        AuthUser user = (AuthUser) authentication.getPrincipal();

        //生成token
        return Jwts.builder()
                .claim("id",user.getId())
                .claim("expireTime",user.getExpireTime().toString())
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
    public AuthUser getUserFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        AuthUser user=new AuthUser(
                claims.get("id",String.class),
                claims.get("username",String.class),
                "just a placeholder",
                (Collection<GrantedAuthority>) claims.get("roles", Collection.class).stream()
                .map(role->{
                  return new SimpleGrantedAuthority((String)role);
                })
                .collect(Collectors.toList()),
                LocalDateTime.parse(claims.get("expireTime",String.class))
        );

        return user;
    }

    /**
     * 验证是否过期
     * @param authToken
     * @return true未过期,false过期
     */
    public boolean validateToken(String authToken) {
        try {
            //获取主体数据,抛出异常则验证失败
            Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(authToken)
                .getBody();
            //判断是否过期
            LocalDateTime time=LocalDateTime.parse(claims.get("expireTime",String.class));
            if(LocalDateTime.now().isAfter(time)) {//过期
                return false;
            }else {//未过期
                return true;
            }
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
