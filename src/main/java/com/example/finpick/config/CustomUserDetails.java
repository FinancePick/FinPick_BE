package com.example.finpick.config;

import com.example.finpick.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;  // com.example.sa_project.domain.user.User 객체

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();  // userId 반환
    }

    public String getLevel(){return user.getLevel();}

    @Override
    public List<GrantedAuthority> getAuthorities() {
        // 권한을 반환합니다. 예시로 "ROLE_USER"만 설정.
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // 사용자 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getUsername();  // 사용자 이름
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 자격 증명 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true;  // 계정 활성화 여부
    }
}
