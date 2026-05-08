package dev.jramde.saas.dto.response;

import dev.jramde.saas.entity.enums.ETypeMvmt;
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
    private String id;
    private ETypeMvmt typeMvmt;
    private Integer quantity;
    private LocalDateTime dateMvmt;
    private String comment;
}
