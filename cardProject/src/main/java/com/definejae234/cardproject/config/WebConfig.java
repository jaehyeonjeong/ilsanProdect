package com.definejae234.cardproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.path}")
    private String uploadDir; // 예: C:/upload/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + uploadDir);

        // 카드 등록한 이미지는 여기 storage 저장
        registry.addResourceHandler("/storage/**")
                .addResourceLocations("file:///" + uploadDir + "card/");
    }
}
