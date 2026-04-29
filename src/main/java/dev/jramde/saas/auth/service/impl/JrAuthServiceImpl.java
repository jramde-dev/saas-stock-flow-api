package dev.jramde.saas.auth.service.impl;

import dev.jramde.saas.auth.dto.request.JrLoginRequest;
import dev.jramde.saas.auth.dto.response.JrLoginResponse;
import dev.jramde.saas.auth.service.IAuthService;
import dev.jramde.saas.entity.JrUser;
import dev.jramde.saas.security.JrJwtTokenService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JrAuthServiceImpl implements IAuthService {
    private final AuthenticationManager authManager;
    private final JrJwtTokenService jwtTokenService;

    @Override
    public JrLoginResponse login(JrLoginRequest loginRequest) {
        final Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        final JrUser user = (JrUser) authentication.getPrincipal();
        final String token = jwtTokenService.generateAccessToken(
                Objects.requireNonNull(user).getTenantId(), user.getId(), user.getRole().name());
        final String tokenType = "Bearer";

        return JrLoginResponse.builder()
                .accessToken(token)
                .tokenType(tokenType)
                .build();
    }
}
