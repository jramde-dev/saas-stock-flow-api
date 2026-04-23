package dev.jramde.saas.auth.repository;

import dev.jramde.saas.entity.JrUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrUserRepository extends JpaRepository<JrUser, String> {

    Optional<JrUser> findByUsername(String username);

    boolean existsByUsername(String adminUsername);
}
