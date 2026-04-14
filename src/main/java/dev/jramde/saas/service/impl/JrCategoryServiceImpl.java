package dev.jramde.saas.service.impl;

import dev.jramde.saas.common.JrPageResponse;
import dev.jramde.saas.dto.request.JrCategoryRequest;
import dev.jramde.saas.dto.response.JrCategoryResponse;
import dev.jramde.saas.entity.JrCategory;
import dev.jramde.saas.mapper.JrCategoryMapper;
import dev.jramde.saas.repository.JrCategoryRepository;
import dev.jramde.saas.repository.JrProductRepository;
import dev.jramde.saas.service.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JrCategoryServiceImpl implements ICategoryService {
    private final JrCategoryRepository categoryRepository;
    private final JrProductRepository productRepository;
    private final JrCategoryMapper mapper;

    @Override
    public void create(JrCategoryRequest request) {
        categoryRepository.findByNameIgnoreCase(request.getName()).ifPresent(category -> {
            throw new RuntimeException("Category already exists.");
        });
        categoryRepository.save(mapper.maps(request));
    }

    @Override
    public void update(String id, JrCategoryRequest request) {
        categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("This category does not exist.")
        );

        categoryRepository.findByNameIgnoreCase(request.getName()).ifPresent(category -> {
            throw new RuntimeException("Category with name " + request.getName() + " already exists.");
        });

        JrCategory category = mapper.maps(request);
        category.setId(id);
        categoryRepository.save(category);
    }

    @Override
    public JrPageResponse<JrCategoryResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<JrCategory> categories = categoryRepository.findAll(pageRequest);
        final Page<JrCategoryResponse> responses = categories.map(mapper::maps);
        return JrPageResponse.of(responses);
    }


    @Override
    public JrCategoryResponse findById(String id) {
        return categoryRepository.findById(id)
                .map(mapper::maps)
                .orElseThrow(() -> new EntityNotFoundException("This category does not exist."));
    }

    @Override
    public void delete(String id) {
        categoryRepository.findById(id).ifPresent(category -> {
            productRepository.deleteAll(category.getProducts());
            categoryRepository.delete(category);
        });
    }
}
