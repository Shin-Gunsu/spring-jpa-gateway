package com.nhnacademy.gateway.config;


import com.nhnacademy.gateway.filter.UserAuthenticationFilter;
import com.nhnacademy.gateway.model.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Configuration
public class SecurityConfig {
    final
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    final
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    final
    ApiAuthenticationProvider apiAuthenticationProvider;


    UserAuthenticationFilter userAuthenticationFilter;

    final
    RedisTemplate<String, Object> sessionRedisTemplate;
    final
    UserService userService;

    public SecurityConfig(CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, RedisTemplate<String, Object> sessionRedisTemplate, UserService userService, ApiAuthenticationProvider apiAuthenticationProvider) {
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.sessionRedisTemplate = sessionRedisTemplate;
        this.userService = userService;
        this.apiAuthenticationProvider = apiAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomLogoutHandler customLogoutHandler) throws Exception {



        http.csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(apiAuthenticationProvider)
                .formLogin((formLogin) ->
                formLogin.loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/auth/login/process")
                        .successHandler(customAuthenticationSuccessHandler)
//                        .failureHandler(customAuthenticationFailureHandler)

        ).authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/member/**").hasAnyRole("ADMIN","MEMBER")
                        .requestMatchers("/google/**").hasAnyRole("ADMIN","GOOGLE")
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/members/**").permitAll()
                        .requestMatchers("/auth/blocked").permitAll()
                        .requestMatchers("/user/register").permitAll()
                        .anyRequest().authenticated()

        ).logout((logout)->logout.deleteCookies("A-COOKIE", "B-COOKIE")
                .invalidateHttpSession(true)
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/")
                        .logoutSuccessHandler(customLogoutHandler)

        )
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"));


        http.addFilterBefore(new UserAuthenticationFilter(sessionRedisTemplate, userService, passwordEncoder()), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }




    @Bean
    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}