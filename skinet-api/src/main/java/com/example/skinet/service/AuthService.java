package com.example.skinet.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.skinet.config.AppConfigProperties;
import com.example.skinet.core.entity.*;
import com.example.skinet.error.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppConfigProperties appConfigProperties;
    private final UserService userService;

    public UserDTO getUserByEmail(String email) {
        AppUser appUser = userService.getUser(email).orElseThrow(() -> ApiException.emailNotFound(email));
        return createUserDto(appUser);
    }

    public UserDTO loginUser(LoginDTO loginDTO) {
        log.info("Trying to log in with email {}", loginDTO.getEmail());

        Authentication authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        AppUser user = userService.getUser(((User) authResult.getPrincipal()).getUsername())
                .orElseThrow();

        return createUserDto(user);
    }

    @Transactional
    public UserDTO registerUser(RegisterDTO registerDTO) {
        log.info("Trying to register with email {}", registerDTO.getEmail());

        AppUser user = userService.createUser(new AppUser(registerDTO.getEmail(), registerDTO.getName()),
                registerDTO.getPassword());
        userService.addRoleToUser(user.getEmail(), "ROLE_USER");

        return createUserDto(user);
    }

    private UserDTO createUserDto(AppUser user) {
        AppConfigProperties.JwtConfigProperties jwtConfig = appConfigProperties.getJwt();
        Date expiresAt = Date.from(LocalDateTime.now(ZoneOffset.UTC)
                .plusDays(jwtConfig.getDaysToExpire())
                .toInstant(ZoneOffset.UTC));

        Algorithm algorithm = Algorithm.HMAC512(jwtConfig.getSecret());

        String accessToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(expiresAt)
                .withIssuer(jwtConfig.getIssuer())
                .withClaim("roles", user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .sign(algorithm);

        return new UserDTO(user.getEmail(), user.getName(), accessToken);
    }
}
