package com.project.filters;


import com.project.config.SecurityExceptionHandlerConfig;
import com.project.model.enums.ErrorCode;
import com.project.response.ErrorResponse;
import com.project.service.CustomUserDetailsService;
import com.project.utils.ApiUtils;
import com.project.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SecurityExceptionHandlerConfig securityExceptionHandlerConfig;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (Arrays.stream(ApiUtils.PERMIT_ALL).anyMatch(value -> request.getServletPath().startsWith(value.replace("/**", "")))) {
                filterChain.doFilter(request, response);
            } else {
               securityExceptionHandlerConfig.handle(response, new ErrorResponse(ErrorCode.UA));
            }
            return;
        }
        jwt = authHeader.substring(7);
        try {
            username = jwtUtils.extractEmail(jwt);
        } catch (ExpiredJwtException e) {
            securityExceptionHandlerConfig.handle(response, new ErrorResponse(ErrorCode.EXT));
            return;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                request.setAttribute("id", jwtUtils.getIdFromJwtToken(jwt));
            }

        }
        filterChain.doFilter(request, response);
    }
}
