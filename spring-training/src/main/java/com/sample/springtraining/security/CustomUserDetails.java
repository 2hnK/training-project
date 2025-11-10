package com.sample.springtraining.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sample.springtraining.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Member member;

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return !member.isAccountCredentialsExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !member.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !member.isAccountCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return member.isVerified();
    }

    // Member 객체 직접 접근을 위한 getter 메서드들
    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getNickname() {
        return member.getNickname();
    }
}
