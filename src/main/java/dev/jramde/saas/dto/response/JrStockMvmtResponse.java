package dev.jramde.saas.dto.response;

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
public class JrStockMvmtResponse {
    private String typeMvmt;
    private Integer quantity;
    private LocalDateTime dateMvmt;
    private String comment;
}
