package dev.jramde.saas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
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
@Table(name = "jr_products")
public class JrProduct extends JrAbstractAuditEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "reference")
    private String reference;

    @Column(name = "description")
    private String description;

    /** Seuil d'alerte */
    @Column(name = "alert_threshold")
    private Integer alertThreshold;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private JrCategory category;

    @OneToMany(mappedBy = "product")
    private List<JrStockMvmt> movements;
}
