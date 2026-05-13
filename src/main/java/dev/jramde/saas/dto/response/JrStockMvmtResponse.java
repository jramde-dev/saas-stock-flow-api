package dev.jramde.saas.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.jramde.saas.entity.enums.ETypeMvmt;
import java.time.LocalDate;
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

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateMvmt;
    private String comment;
}
