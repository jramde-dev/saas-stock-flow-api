package dev.jramde.saas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Product name is required.")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 characters.")
    private String name;

    @NotBlank(message = "Product reference is required.")
    private String reference;
    private String description;

    @Positive(message = "The alert threshold must be a positive number.")
    private Integer alertThreshold;

    @NotBlank(message = "The price is required.")
    private BigDecimal price;

    @NotBlank(message = "The category is required.")
    private String categoryId;
}
