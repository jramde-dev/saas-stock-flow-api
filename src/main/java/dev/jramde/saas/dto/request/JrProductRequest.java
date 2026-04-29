package dev.jramde.saas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Note: The @NotBlank only works with String.
 * It checks if the string is not null, not empty, and not containing whitespace.
 * Using it in BigDecimal will throw an exception.
 */

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

    @NotNull(message = "The price is required.")
    @Positive(message = "The price must be a positive number.")
    private BigDecimal price;

    @NotBlank(message = "The category is required.")
    private String categoryId;
}
