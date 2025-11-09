package com.sample.springtraining.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sample.springtraining.repositories.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        com.sample.springtraining.models.User user = userRepository.findByLoginId(loginId);

        if (user == null) {
            throw new UsernameNotFoundException("User with loginId " + loginId + " not found");
        }
        return User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .disabled(!user.isVerified())
                .accountExpired(user.isAccountCredentialsExpired())
                .accountLocked(user.isLocked())
                .roles("USER")
                .build();
    }
}
