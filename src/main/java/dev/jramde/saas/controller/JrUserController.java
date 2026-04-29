package dev.jramde.saas.controller;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrUserRequest;
import dev.jramde.saas.dto.response.JrUserResponse;
import dev.jramde.saas.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class JrUserController {
    private final IUserService userService;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid JrUserRequest request) {
        userService.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String userId, @RequestBody @Valid JrUserRequest request) {
        userService.update(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<JrPageResponse<JrUserResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ADMINISTRATOR')")
    public ResponseEntity<JrUserResponse> findById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/enable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> enable(@PathVariable String userId) {
        userService.enable(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/{userId}/disable")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public ResponseEntity<Void> disable(@PathVariable String userId) {
        userService.disable(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
