package com.example.ooad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // Bước 1: Lấy từ biến môi trường của hệ thống (Docker/Render set cái này)
        String feOrigin = System.getenv("FE_ORIGIN");

        // Bước 2: Nếu hệ thống không có, mới tìm trong file .env
        if (feOrigin == null || feOrigin.isEmpty()) {
            feOrigin = dotenv.get("FE_ORIGIN");
        }

        // Bước 3: Nếu cả 2 đều không có, dùng mặc định localhost:3000 (Chống lỗi null)
        if (feOrigin == null || feOrigin.isEmpty()) {
            feOrigin = "http://localhost:5173";
        }

        registry.addMapping("/**") // Apply to all paths
                .allowedOrigins(feOrigin.split(",")) // Cho phép nhiều domain (ngăn cách bằng dấu phẩy)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allowed headers
                .allowCredentials(true) // Allow credentials
                .maxAge(3600);
    }
}
