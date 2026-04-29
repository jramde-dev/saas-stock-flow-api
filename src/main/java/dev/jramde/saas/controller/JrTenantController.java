package dev.jramde.saas.controller;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.response.JrTenantResponse;
import dev.jramde.saas.service.ITenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant", description = "Tenant API")
public class JrTenantController {
    private final ITenantService tenantService;

    @PostMapping("/{tenantId}/approve")
    public ResponseEntity<Void> approveTenant(@PathVariable String tenantId) {
        tenantService.approveTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{tenantId}/activate")
    public ResponseEntity<Void> activateTenant(@PathVariable String tenantId) {
        tenantService.activateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{tenantId}/deactivate")
    public ResponseEntity<Void> deactivateTenant(@PathVariable String tenantId) {
        tenantService.deactivateTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{tenantId}/suspend")
    public ResponseEntity<Void> suspendTenant(@PathVariable String tenantId) {
        tenantService.suspendTenant(tenantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<JrPageResponse<JrTenantResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(tenantService.findAll(page, size));
    }
}
