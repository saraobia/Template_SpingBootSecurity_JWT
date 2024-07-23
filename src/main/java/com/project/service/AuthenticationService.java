package com.project.service;

import com.project.exception.AuthException;
import com.project.model.AuthRequest;
import com.project.model.User;
import com.project.model.dto.AuthenticationResponse;
import com.project.model.enums.ErrorCode;
import com.project.repository.RoleRepository;
import com.project.repository.UserRepository;
import com.project.response.ErrorResponse;
import com.project.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public AuthenticationResponse authentication(AuthRequest request) {
        //Verifica le credenziali dell'utente e restituisce un token di autenticazione
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(new ErrorResponse(ErrorCode.EUN, "user with email not found")));
        return AuthenticationResponse.builder()
                .accessToken(jwtUtils.generateToken(user))
                .refreshToken(jwtUtils.generateRefreshToken(user))
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        User user = customUserDetailsService.getUserFromToken(request);
        return AuthenticationResponse.builder().accessToken(jwtUtils.generateToken(user))
                                               .refreshToken(jwtUtils.generateRefreshToken(user))
                                               .build();
    }
}
