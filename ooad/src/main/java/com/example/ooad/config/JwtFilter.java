package com.example.ooad.config;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ooad.service.auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
@Order(1)
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailService;
    private final JwtService jwtService;
    public JwtFilter(@Lazy UserDetailsService userDetailService, JwtService jwtService){
        this.userDetailService= userDetailService;
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token=null;
        String tenTaiKhoan=null;
        if(authHeader!=null&&authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            tenTaiKhoan = jwtService.extractTenDangNhap(token);
        }
        if(tenTaiKhoan!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(tenTaiKhoan);
            if(jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
