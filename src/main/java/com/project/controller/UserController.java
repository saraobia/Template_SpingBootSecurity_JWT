package com.project.controller;

import com.project.exception.UserNotFoundException;
import com.project.model.AuthRequest;
import com.project.model.User;
import com.project.response.SuccessResponse;
import com.project.service.UserService;
import com.project.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/user-info")
    public ResponseEntity<SuccessResponse<User>> getUserInfo(HttpServletRequest request) throws UserNotFoundException {

        String id = (String) request.getAttribute("id");

        User user = userService.getUserInfoById(id)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato"));

        return new ResponseEntity<>(new SuccessResponse<>(user), HttpStatus.OK);
    }
}
