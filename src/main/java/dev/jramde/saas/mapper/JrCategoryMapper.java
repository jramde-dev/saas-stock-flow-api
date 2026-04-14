package dev.jramde.saas.mapper;

import dev.jramde.saas.dto.request.JrCategoryRequest;
import dev.jramde.saas.dto.response.JrCategoryResponse;
import dev.jramde.saas.entity.JrCategory;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class JrCategoryMapper {

    public JrCategory maps(JrCategoryRequest request) {
        return JrCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public JrCategoryResponse maps(JrCategory entity) {
        return JrCategoryResponse.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .totalProducts(entity.getProducts().size())
                .build();
    }

    public List<JrCategoryResponse> toCategoryList(List<JrCategory> entities) {
        return entities.stream()
                .map(this::maps)
                .collect(Collectors.toList());
    }
}
