package com.nhnacademy.gateway.config;


import com.nhnacademy.gateway.filter.UserAuthenticationFilter;
import com.nhnacademy.gateway.service.UserService;
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


    UserAuthenticationFilter userAuthenticationFilter;

    final
    RedisTemplate<String, Object> sessionRedisTemplate;

    final UserService userService;

    public SecurityConfig(CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,  RedisTemplate<String, Object> sessionRedisTemplate,UserService userService) {
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.sessionRedisTemplate = sessionRedisTemplate;
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomLogoutHandler customLogoutHandler) throws Exception {



        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin((formLogin) ->
                formLogin.loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login/process")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)

        ).authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/main").hasRole("REGISTERED")
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated()

        ).logout((logout)->logout.deleteCookies("A-COOKIE", "B-COOKIE")
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                        .logoutSuccessHandler(customLogoutHandler)

        )
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"));


        http.addFilterBefore(new UserAuthenticationFilter(sessionRedisTemplate, passwordEncoder(), userService), UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }


    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_MEMBER"); //// ROLE_ADMIN은 ROLE_MEMBER보다 상위
    }

    @Bean
    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}