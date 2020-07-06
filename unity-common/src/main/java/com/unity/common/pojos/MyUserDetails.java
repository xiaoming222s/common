package com.unity.common.pojos;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 用户安全的具体类
 * <p>
 * create by Jung at 2018-02-20 14:37:26
 */
@Data
public class MyUserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private Long userId;
    private String username;
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(Long userId, String username, String password, boolean enabled,
                         Collection<? extends GrantedAuthority> authorities) {
        super();
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
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
        return true;
    }
}