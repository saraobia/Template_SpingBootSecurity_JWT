package com.project.controller;

import com.project.exception.UserGenericsException;
import com.project.exception.UserMailWrongException;
import com.project.exception.UserNotFoundException;
import com.project.exception.UserPasswordWrongException;
import com.project.model.AuthRequest;
import com.project.model.dto.AuthResponse;
import com.project.response.SuccessResponse;
import com.project.service.AuthService;
import com.project.service.UserService;
import com.project.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<AuthResponse>> login(@RequestBody AuthRequest request) throws UserNotFoundException, UserPasswordWrongException, UserGenericsException, UserMailWrongException {
         return new ResponseEntity<>(new SuccessResponse<>(authService.authentication(request)), HttpStatus.OK);
    }

}



