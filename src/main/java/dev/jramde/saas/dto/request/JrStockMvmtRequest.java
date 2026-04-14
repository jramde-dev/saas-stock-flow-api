package dev.jramde.saas.dto.request;

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
    private String typeMvmt;
    private Integer quantity;
    private LocalDateTime dateMvmt;
    private String comment;
    private String productId;
}
