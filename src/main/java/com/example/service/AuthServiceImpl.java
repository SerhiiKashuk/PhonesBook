package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.repository.UserRepository;



@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getAuthenticatedUser() {
        return userRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}