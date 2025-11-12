package com.definejae234.cardproject.config;

import com.definejae234.cardproject.member.service.CustomUserDetailsService;
import com.definejae234.cardproject.member.service.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

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
                                        "/card/firstPage",
                                        "/card/secondPage",
                                        "/css/**", "/js/**",
                                        "/script/**", "/json/**",
                                        "/images/**",
                                        "/member/idCheck",
                                        "/member/find-id",
                                        "/member/find-password"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
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
                .oauth2Login(oauth2 -> {
                    oauth2.loginPage("/member/login")
                            .defaultSuccessUrl("/", true)
                            .userInfoEndpoint(userInfo -> {
                                userInfo.userService(oAuth2DetailsService);
                            });
                })
                .logout(logout -> logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID","remember-me")
                        .permitAll()
                )
                .rememberMe(
                        remember-> remember
                                .userDetailsService(customUserDetailsService)
                                .tokenRepository(persistentTokenRepository())
                                .rememberMeParameter("remember-me")
                                .rememberMeCookieName("remember-me")
                                .tokenValiditySeconds(60*60*24*14)
                                .key("To-remember-me-secret-key")
                                .useSecureCookie(false)
                                .alwaysRemember(false)
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
