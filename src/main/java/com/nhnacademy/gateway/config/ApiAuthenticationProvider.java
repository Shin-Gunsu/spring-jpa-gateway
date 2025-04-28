package com.nhnacademy.gateway.config;


import com.nhnacademy.gateway.model.user.AuthedUser;
import com.nhnacademy.gateway.model.user.User;
import com.nhnacademy.gateway.model.user.UserLoginRequest;
import com.nhnacademy.gateway.model.user.UserView;
import com.nhnacademy.gateway.model.user.service.UserService;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final
    RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    private static final String LOGIN_URL = "http://localhost:8081/api/users/login";
    private final UserService userService;

    public ApiAuthenticationProvider(RestTemplate restTemplate,
                                     PasswordEncoder passwordEncoder, UserService userService) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String userId = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();


        UserLoginRequest loginReq = new UserLoginRequest(userId, rawPassword);
        ResponseEntity<UserView> resp;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserLoginRequest> req = new HttpEntity<>(loginReq, headers);
            resp = userService.getUser(LOGIN_URL, req);
            System.out.println("test api login response: " + resp.getBody());


        } catch (RestClientException ex) {
            throw new AuthenticationServiceException("계정 서비스 호출 실패", ex);
        }

        if (resp.getStatusCode() != HttpStatus.OK || resp.getBody() == null) {
            throw new BadCredentialsException("아이디 또는 비밀번호 불일치");
        }

        UserView uv = resp.getBody();
        User user = new User();
        user.setEmail(uv.getEmail());
        user.setId(userId);
        user.setStatus(uv.getStatus());
        AuthedUser principal = new AuthedUser(
                user,
                passwordEncoder
        );
        return new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
