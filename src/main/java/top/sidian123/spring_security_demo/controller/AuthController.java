package top.sidian123.spring_security_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import top.sidian123.spring_security_demo.config.AuthUser;
import top.sidian123.spring_security_demo.config.JwtTokenProvider;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author sidian
 * @date 2019/8/2 下午6:12
 */
@RestController
public class AuthController {

    @Value("${app.jwtExpirationInS}")
    private Integer jwtExpirationInS;//过期时间,单位秒

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/login")
    public String authenticateUser(String username, String password) {

        //从用户输入中获取authentication,用于与数据库中的数据比较.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username,password)
        );

        //如果配置了过期时间,则设置,否则使用默认过期时间.
        if(jwtExpirationInS!=null){
            ((AuthUser)authentication.getPrincipal()).setExpireTime(LocalDateTime.now().plusSeconds(jwtExpirationInS));
        }

        //表明验证成功
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //生成token,返回给用户.
        String jwt = tokenProvider.generateToken(authentication);
        return jwt;
    }
}
