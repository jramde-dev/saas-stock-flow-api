package dev.jramde.saas.auth.controller;

import dev.jramde.saas.auth.dto.request.JrLoginRequest;
import dev.jramde.saas.auth.dto.response.JrLoginResponse;
import dev.jramde.saas.auth.service.IAuthService;
import dev.jramde.saas.dto.request.JrRegisterTenantRequest;
import dev.jramde.saas.service.ITenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class JrAuthController {
    private final IAuthService authService;
    private final ITenantService tenantService;

    @PostMapping("/login")
    public ResponseEntity<JrLoginResponse> login(@RequestBody @Valid final JrLoginRequest loginRequest) {
        final JrLoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid final JrRegisterTenantRequest request) {
        tenantService.registerTenant(request);
        return ResponseEntity.ok().build();
    }
}
