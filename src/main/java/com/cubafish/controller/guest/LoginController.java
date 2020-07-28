package com.cubafish.controller.guest;

import com.cubafish.jwt.JwtConfig;
import com.cubafish.jwt.UsernameAndPasswordAuthenticationRequest;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping(LoginController.LOGIN_PATH)
@RequiredArgsConstructor
public class LoginController {

    public static final String LOGIN_PATH = "/guest/login";

    private final AuthenticationManager authenticationManager;

    private final JwtConfig jwtConfig;

    private final SecretKey secretKey;

    @PostMapping()
    public void getAuthorize(
            @RequestBody UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest,
            HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usernameAndPasswordAuthenticationRequest.getUsername(),
                usernameAndPasswordAuthenticationRequest.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);

        String token = Jwts.builder()
                .setSubject(authenticate.getName())
                .claim("authorities", authenticate.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
    }

}
