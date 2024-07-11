package com.project.service;

import com.project.exception.UserException;
import com.project.model.enums.ErrorCode;
import com.project.repository.UserRepository;
import com.project.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserException(
                new ErrorResponse(ErrorCode.EUN, "User not found with email: " + email)
        ));
    }
}
