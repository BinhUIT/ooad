package com.example.ooad.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.ooad.domain.enums.EVaiTro;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtBlackListFilter jwtBlackListFilter;
    public SecurityConfig(JwtFilter jwtFilter, JwtBlackListFilter jwtBlackListFilter) {
        this.jwtFilter= jwtFilter;
        this.jwtBlackListFilter= jwtBlackListFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(httpSecurityCsrfConfigurer->httpSecurityCsrfConfigurer.disable())
        .httpBasic(Customizer.withDefaults()) 
        .authorizeHttpRequests(auth->auth.requestMatchers("/auth/**").permitAll()
        .requestMatchers("/benh_nhan/**").hasAuthority(EVaiTro.BENHNHAN.getLabel())
        .requestMatchers("/nhan_vien_kho/**").hasAnyAuthority(EVaiTro.THUKHO.getLabel())
        .requestMatchers("/le_tan/**").hasAnyAuthority(EVaiTro.LETAN.getLabel())
        .requestMatchers("/bac_si/**").hasAuthority(EVaiTro.BACSI.getLabel())
        .requestMatchers("/admin/**").hasAuthority(EVaiTro.ADMIN.getLabel())
        .anyRequest().authenticated()
        )
        .formLogin(Customizer.withDefaults())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtBlackListFilter, JwtFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
