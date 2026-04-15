package dev.jramde.saas.dto.request;

import dev.jramde.saas.entity.enums.ETypeMvmt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "Type of movement is required.")
    private ETypeMvmt typeMvmt;

    @Positive(message = "Quantity must be a positive number.")
    private Integer quantity;

    @PastOrPresent(message = "Date must be in the past or present.")
    private LocalDateTime dateMvmt;
    private String comment;

    @NotNull(message = "Product is required.")
    private String productId;
}
