package dev.jramde.saas.dto.request;

import dev.jramde.saas.entity.enums.ETypeMvmt;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JrStockMvmtRequest {
    private ETypeMvmt typeMvmt;
    private Integer quantity;
    private LocalDateTime dateMvmt;
    private String comment;

    @NotNull(message = "Product is required.")
    private String productId;
}
