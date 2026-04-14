package dev.jramde.saas.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
public class JrProductRequest {
    @NotNull(message = "Product name is required.")
    private String name;
    private String reference;
    private String description;
    private Integer alertThreshold;

    @NotNull(message = "The price is required.")
    private BigDecimal price;

    @NotNull(message = "The category is required.")
    private String categoryId;
}
