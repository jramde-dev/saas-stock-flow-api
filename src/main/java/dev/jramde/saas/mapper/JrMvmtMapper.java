package dev.jramde.saas.mapper;

import dev.jramde.saas.dto.request.JrStockMvmtRequest;
import dev.jramde.saas.dto.response.JrStockMvmtResponse;
import dev.jramde.saas.entity.JrProduct;
import dev.jramde.saas.entity.JrStockMvmt;
import org.springframework.stereotype.Component;


@Component
public class JrMvmtMapper {

    public JrStockMvmt maps(JrStockMvmtRequest request) {
        return JrStockMvmt.builder()
                .typeMvmt(request.getTypeMvmt())
                .quantity(request.getQuantity())
                .dateMvmt(request.getDateMvmt())
                .comment(request.getComment())
                .deleted(Boolean.FALSE)
                .product(request.getProductId() != null ?
                        JrProduct.builder()
                        .id(request.getProductId())
                        .build() : null)
                .build();
    }

    public JrStockMvmtResponse maps(JrStockMvmt entity) {
        return JrStockMvmtResponse.builder()
                .id(entity.getId())
                .typeMvmt(entity.getTypeMvmt())
                .quantity(entity.getQuantity())
                .dateMvmt(entity.getDateMvmt().toLocalDate())
                .comment(entity.getComment())
                .build();
    }
}
