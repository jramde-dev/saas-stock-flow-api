package dev.jramde.saas.controller;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrStockMvmtRequest;
import dev.jramde.saas.dto.response.JrStockMvmtResponse;
import dev.jramde.saas.service.IStockMvmtService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/stock-movements")
@RequiredArgsConstructor
@Tag(name = "Movement", description = "Movement API")
public class JrMvmtController {
    private final IStockMvmtService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid JrStockMvmtRequest request) {
        service.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{mvmtId}")
    public ResponseEntity<Void> update(@PathVariable String mvmtId, @RequestBody @Valid JrStockMvmtRequest request) {
        service.update(mvmtId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<JrPageResponse<JrStockMvmtResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.findAll(page, size));
    }

    @GetMapping("/{mvmtId}")
    public ResponseEntity<JrStockMvmtResponse> findById(@PathVariable String mvmtId) {
        return ResponseEntity.ok(service.findById(mvmtId));
    }

    @DeleteMapping("/{mvmtId}")
    public ResponseEntity<Void> delete(@PathVariable String mvmtId) {
        service.delete(mvmtId);
        return ResponseEntity.ok().build();
    }
}
