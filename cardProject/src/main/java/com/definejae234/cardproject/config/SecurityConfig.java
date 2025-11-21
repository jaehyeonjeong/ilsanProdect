package com.definejae234.cardproject.config;

import com.definejae234.cardproject.member.service.CustomUserDetailsService;
import com.definejae234.cardproject.member.service.OAuth2DetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication)
                    throws IOException, ServletException {
                Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                if (roles.contains("ROLE_ADMIN")) {
                    response.sendRedirect("/admin/admin");
                } else {
                    response.sendRedirect("/");
                }
            }
        };
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
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
                                        "/card/normal_list",
                                        "/card/*/normal_info",
                                        "/css/**", "/js/**",
                                        "/script/**", "/json/**",
                                        "/images/**",
                                        "/member/idCheck",
                                        "/member/find-id",
                                        "/member/find-password"
                                ).permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/member/login")                              // get 처리
                                .loginProcessingUrl("/member/login")// post 처리
                                .successHandler(customAuthenticationSuccessHandler())
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
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/");
                        })
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
