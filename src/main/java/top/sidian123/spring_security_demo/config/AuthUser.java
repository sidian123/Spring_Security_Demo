package top.sidian123.spring_security_demo.config;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author sidian
 * @date 2019/8/6 下午8:23
 */
@Data
public class AuthUser implements UserDetails {
    private static final long EXPIRE_TIME=60*60;//默认过期时间,单位秒

    private String id;//用于确定用户身份
    private String username;//用于登录验证
    private String password;//用于登录验证
    private LocalDateTime expireTime;//过期时间
    private Collection<GrantedAuthority> authorities;//用户权限



    public AuthUser(String id,String username,String password,Collection<GrantedAuthority> authorities){
        this(id,username,password,authorities,null);
    }

    /**
     * 构造函数.当expireTime为null时,将当前时间+过期时长作为过期日期
     * @param id
     * @param username
     * @param password
     * @param authorities
     * @param expireTime
     */
    public AuthUser(String id,String username,String password,Collection<GrantedAuthority> authorities,LocalDateTime expireTime){
        this.id=id;
        this.username=username;
        this.password=password;
        this.authorities=authorities;
        if(expireTime!=null){
            this.expireTime=expireTime;
        }else{
            this.expireTime=LocalDateTime.now().plusSeconds(EXPIRE_TIME);
        }
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if(LocalDateTime.now().isBefore(expireTime)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
