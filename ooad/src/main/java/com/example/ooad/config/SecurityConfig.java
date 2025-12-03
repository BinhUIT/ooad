package com.example.ooad.config;



import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.ooad.domain.enums.ERole;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private Dotenv dotenv = Dotenv.load();
    private final JwtFilter jwtFilter;
    private final JwtBlackListFilter jwtBlackListFilter;
    public SecurityConfig(JwtFilter jwtFilter, JwtBlackListFilter jwtBlackListFilter) {
        this.jwtFilter= jwtFilter;
        this.jwtBlackListFilter= jwtBlackListFilter;
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(dotenv.get("FE_ORIGIN")));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(httpSecurityCsrfConfigurer->httpSecurityCsrfConfigurer.disable()).cors(cors->cors.configurationSource(corsConfigurationSource()))
        .httpBasic(Customizer.withDefaults()) 
        .authorizeHttpRequests(auth->auth.requestMatchers("/auth/**","/receptionist/**",
        "/swagger-ui/**","/v3/api-docs/**","/swagger-resources/**","/webjars/**","/unsecure/**").permitAll()
        // API profile /account/me - Tất cả user đã đăng nhập đều truy cập được
        .requestMatchers("/account/**").authenticated()
        .requestMatchers("/patient/**").hasAuthority(ERole.PATIENT.name())
        .requestMatchers("/store_keeper/**").hasAnyAuthority(ERole.WAREHOUSE_STAFF.name())
        //.requestMatchers("/receptionist/**").hasAnyAuthority(ERole.RECEPTIONIST.name())
        .requestMatchers("/doctor/**").hasAuthority(ERole.DOCTOR.name())
        .requestMatchers("/admin/**").hasAuthority(ERole.ADMIN.name())
        .anyRequest().authenticated()
        )
        
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
