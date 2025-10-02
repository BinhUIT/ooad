package com.example.ooad.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ooad.domain.entity.UnusedAccessToken;
import com.example.ooad.service.auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtBlackListFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    public JwtBlackListFilter(JwtService jwtService) {
        this.jwtService= jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader!=null&&authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UnusedAccessToken unusedAccessToken = jwtService.getUnusedAccessTokenByToken(token);
            if(unusedAccessToken!=null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"Token is blacklisted\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
