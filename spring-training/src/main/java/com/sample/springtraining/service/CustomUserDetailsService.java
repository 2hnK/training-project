package com.sample.springtraining.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sample.springtraining.entity.Member;
import com.sample.springtraining.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = userRepository.findByLoginId(loginId);

        if (member == null) {
            throw new UsernameNotFoundException("Member with loginId " + loginId + " not found");
        }
        return User.builder()
                .username(member.getLoginId())
                .password(member.getPassword())
                .disabled(!member.isVerified())
                .accountExpired(member.isAccountCredentialsExpired())
                .accountLocked(member.isLocked())
                .roles("USER")
                .build();
    }
}
