package dev.jramde.saas.controller;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrCategoryRequest;
import dev.jramde.saas.dto.response.JrCategoryResponse;
import dev.jramde.saas.service.ICategoryService;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category API")
public class JrCategoryController {
    private final ICategoryService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid JrCategoryRequest request) {
        service.create(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> update(@PathVariable String categoryId, @RequestBody @Valid JrCategoryRequest request) {
        service.update(categoryId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<JrPageResponse<JrCategoryResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.findAll(page, size));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<JrCategoryResponse> findById(@PathVariable String categoryId) {
        return ResponseEntity.ok(service.findById(categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable String categoryId) {
        service.delete(categoryId);
        return ResponseEntity.ok().build();
    }
}
