package com.cubafish.controller.guest;

import com.cubafish.jwt.JwtConfig;
import com.cubafish.jwt.UsernameAndPasswordAuthenticationRequest;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping(LoginController.LOGIN_PATH)
@RequiredArgsConstructor
public class LoginController {

    private static final Logger log = Logger.getLogger(LoginController.class);
    public static final String LOGIN_PATH = "/guest/login";

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @PostMapping()
    public void getAuthorize(
            @RequestBody UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest,
            HttpServletResponse response) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                usernameAndPasswordAuthenticationRequest.getUsername(),
                usernameAndPasswordAuthenticationRequest.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (authenticate.isAuthenticated()) {
            String token = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .claim("authorities", authenticate.getAuthorities())
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now()
                            .plusDays(jwtConfig.getTokenExpirationAfterDays())))
                    .signWith(secretKey)
                    .compact();
            response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
            log.info("user: " + usernameAndPasswordAuthenticationRequest.getUsername()
                    + " is successfully authorized!");
        } else {
            log.error("user: " + usernameAndPasswordAuthenticationRequest.getUsername() + " could not to log in");
        }
    }
}
