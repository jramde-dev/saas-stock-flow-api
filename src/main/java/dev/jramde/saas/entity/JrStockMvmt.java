package dev.jramde.saas.entity;

import dev.jramde.saas.entity.enums.ETypeMvmt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "jr_stock_mvmts")
public class JrStockMvmt extends JrAbstractAuditEntity {

    @Column(name = "type_mvmt")
    @Enumerated(EnumType.STRING)
    private ETypeMvmt typeMvmt;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "date_mvmt")
    private LocalDateTime dateMvmt;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private JrProduct product;
}
