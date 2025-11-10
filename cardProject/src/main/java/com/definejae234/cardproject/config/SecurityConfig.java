package com.definejae234.cardproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/",
                                        "/index",
                                        "/member/signup",
                                        "/card/home",
                                        "/card/script",
                                        "/card/list",
                                        "/css/**", "/js/**",
                                        "/script/**", "/json/**",
                                        "/images/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form ->
                        form
                                .loginPage("/member/login")                              // get 처리
                                .loginProcessingUrl("/member/login")                      // post 처리
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/member/login?error=true") // 리다이렉트 로그인 에러 처리
                                .usernameParameter("userID")                              // form안에 name값은 userID
                                .passwordParameter("userPW")                              // form안에 패스워드 항목
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/member/logout")
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                                .logoutSuccessUrl("/")
                );
        return http.build();
    }
}
