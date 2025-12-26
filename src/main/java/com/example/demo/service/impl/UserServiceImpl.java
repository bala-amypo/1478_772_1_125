package com.example.demo.service.impl;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public User register(RegisterRequest request) {
        // Check if email already exists - MUST use exact message "Email already in use"
        userRepository.findByEmailIgnoreCase(request.getEmail().trim())
            .ifPresent(existing -> {
                throw new BadRequestException("Email already in use");
            });
        
        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole().trim() : "USER");
        user.setActive(true);
        
        return userRepository.save(user);
    }
    
    @Override
    public User findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new BadRequestException("User not found with email: " + email));
    }
}