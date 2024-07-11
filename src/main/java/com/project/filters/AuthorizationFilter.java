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
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityExceptionHandlerConfig securityExceptionHandlerConfig;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if(Arrays.stream(ApiUtils.PERMIT_ALL).anyMatch(value -> request.getServletPath().startsWith(value.replace(
                    "/**", ""
            )))
                    ) {
                filterChain.doFilter(request, response);
            } else {
                String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
                if(authorization != null && authorization.startsWith("Bearer ")) {
                    String token = authorization.substring("Bearer ".length());
                    String email = jwtUtils.extractEmail(token);
                    UserDetails user = userDetailsService.loadUserByUsername(email);
                    if (jwtUtils.isTokenValid(token, user)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, user.getPassword(), user.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        request.setAttribute("id", jwtUtils.getIdFromJwtToken(token));

                        doFilter(request, response, filterChain);
                    }
                } else {
                    securityExceptionHandlerConfig.handle(response, new ErrorResponse(ErrorCode.UA));
                }
            }
        } catch (ExpiredJwtException e) {
            securityExceptionHandlerConfig.handle(response, new ErrorResponse(ErrorCode.EXT));
        }

    }
}
