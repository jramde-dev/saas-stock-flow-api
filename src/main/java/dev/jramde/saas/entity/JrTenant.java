package dev.jramde.saas.entity;

import dev.jramde.saas.entity.enums.ETenantStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
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
@Table(name = "jr_tenants")
public class JrTenant extends JrAbstractAuditEntity {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ETenantStatus status = ETenantStatus.PENDING;

    // Company admin initial credentials
    @Column(name = "admin_first_name")
    private String adminFirstName;

    @Column(name = "admin_last_name")
    private String adminLastName;

    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "admin_username")
    private String adminUsername;

    @Column(name = "admin_password")
    private String adminPassword;

    public String getAdminFullName() {
        return this.adminFirstName + " " + this.adminLastName;
    }
}
