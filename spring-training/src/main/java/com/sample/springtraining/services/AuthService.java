package com.sample.springtraining.services;

import org.springframework.stereotype.Service;

import com.sample.springtraining.dto.auth.LoginRequest;

@Service
public class AuthService {

    public String authenticate(LoginRequest request) {
        return "token";
    }
}
