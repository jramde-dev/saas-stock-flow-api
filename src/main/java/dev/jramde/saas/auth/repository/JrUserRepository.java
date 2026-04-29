package dev.jramde.saas.auth.repository;

import dev.jramde.saas.entity.JrUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JrUserRepository extends JpaRepository<JrUser, String> {

    Optional<JrUser> findByUsername(String username);

    Optional<JrUser> findByIdAndDeletedFalse(String id);

    @Query("SELECT u FROM JrUser u WHERE u.tenant.id = :tenantId AND u.deleted = false")
    Page<JrUser> findAllByTenantId(String tenantId, Pageable pageable);

    boolean existsByUsername(String adminUsername);

    boolean existsByEmail(String email);
}
