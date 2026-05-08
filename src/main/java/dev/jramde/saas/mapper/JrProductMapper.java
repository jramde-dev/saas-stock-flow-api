package dev.jramde.saas.mapper;

import dev.jramde.saas.dto.request.JrProductRequest;
import dev.jramde.saas.dto.response.JrProductResponse;
import dev.jramde.saas.entity.JrCategory;
import dev.jramde.saas.entity.JrProduct;
import org.springframework.stereotype.Component;


@Component
public class JrProductMapper {

    public JrProduct maps(JrProductRequest request) {
        return JrProduct.builder()
                .name(request.getName())
                .reference(request.getReference())
                .description(request.getDescription())
                .alertThreshold(request.getAlertThreshold())
                .price(request.getPrice())
                .category(request.getCategoryId() != null ?
                        JrCategory.builder()
                        .id(request.getCategoryId())
                        .build() : null)
                .build();
    }

    public JrProductResponse maps(JrProduct entity) {
        return JrProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .reference(entity.getReference())
                .description(entity.getDescription())
                .alertThreshold(entity.getAlertThreshold())
                .price(entity.getPrice())
                .categoryName(entity.getCategory().getName())
                // .availableQuantity() to be implemented later
                .build();
    }
}
