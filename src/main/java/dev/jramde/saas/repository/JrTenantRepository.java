package dev.jramde.saas.repository;

import dev.jramde.saas.entity.JrTenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrTenantRepository extends JpaRepository<JrTenant, String> {
    boolean existsByCompanyCode(String companyCode);

    boolean existsByCompanyEmail(String email);

    boolean existsByAdminEmail(String adminEmail);

    boolean existsByAdminUsername(String adminUsername);
}