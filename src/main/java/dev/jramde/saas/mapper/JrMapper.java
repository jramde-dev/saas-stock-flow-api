package dev.jramde.saas.mapper;

import dev.jramde.saas.dto.request.JrCategoryRequest;
import dev.jramde.saas.dto.request.JrProductRequest;
import dev.jramde.saas.dto.request.JrStockMvmtRequest;
import dev.jramde.saas.dto.response.JrCategoryResponse;
import dev.jramde.saas.dto.response.JrProductResponse;
import dev.jramde.saas.dto.response.JrStockMvmtResponse;
import dev.jramde.saas.entity.JrCategory;
import dev.jramde.saas.entity.JrProduct;
import dev.jramde.saas.entity.JrStockMvmt;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * Cette classe est destinée au mapping de tous les objets de l'application.
 */
@Component
public class JrMapper {

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
                .name(entity.getName())
                .reference(entity.getReference())
                .description(entity.getDescription())
                .alertThreshold(entity.getAlertThreshold())
                .price(entity.getPrice())
                .categoryName(entity.getCategory().getName())
                // .availableQuantity() to be implemented later
                .build();
    }

    public List<JrProductResponse> toProductList(List<JrProduct> entities) {
        return entities.stream()
                .map(this::maps)
                .collect(Collectors.toList());
    }

    public JrStockMvmt maps(JrStockMvmtRequest request) {
        return JrStockMvmt.builder()
                .typeMvmt(request.getTypeMvmt())
                .quantity(request.getQuantity())
                .dateMvmt(request.getDateMvmt())
                .comment(request.getComment())
                .product(request.getProductId() != null ?
                        JrProduct.builder()
                        .id(request.getProductId())
                        .build() : null)
                .build();
    }

    public JrStockMvmtResponse maps(JrStockMvmt entity) {
        return JrStockMvmtResponse.builder()
                .typeMvmt(entity.getTypeMvmt())
                .quantity(entity.getQuantity())
                .dateMvmt(entity.getDateMvmt())
                .comment(entity.getComment())
                .build();
    }

    public List<JrStockMvmtResponse> toStockMvmtList(List<JrStockMvmt> entities) {
        return entities.stream()
                .map(this::maps)
                .collect(Collectors.toList());
    }
}
