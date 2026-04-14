package dev.jramde.saas.controller;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrProductRequest;
import dev.jramde.saas.dto.response.JrProductResponse;
import dev.jramde.saas.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class JrProductController {
    private final IProductService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid JrProductRequest request) {
        service.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> update(@PathVariable String productId, @RequestBody @Valid JrProductRequest request) {
        service.update(productId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<JrPageResponse<JrProductResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.findAll(page, size));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<JrProductResponse> findById(@PathVariable String productId) {
        return ResponseEntity.ok(service.findById(productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable String productId) {
        service.delete(productId);
        return ResponseEntity.ok().build();
    }
}
