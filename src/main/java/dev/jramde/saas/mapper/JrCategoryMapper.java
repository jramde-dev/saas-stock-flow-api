package dev.jramde.saas.mapper;

import dev.jramde.saas.dto.request.JrCategoryRequest;
import dev.jramde.saas.dto.response.JrCategoryResponse;
import dev.jramde.saas.entity.JrCategory;
import org.springframework.stereotype.Component;

@Component
public class JrCategoryMapper {

    public JrCategory maps(JrCategoryRequest request) {
        return JrCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deleted(Boolean.FALSE)
                .build();
    }

    public JrCategoryResponse maps(JrCategory entity) {
        return JrCategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .totalProducts(entity.getProducts().size())
                .build();
    }
}
