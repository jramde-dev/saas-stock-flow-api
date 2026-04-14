package dev.jramde.saas.repository;

import dev.jramde.saas.entity.JrCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrCategoryRepository extends JpaRepository<JrCategory, String> {

    Optional<JrCategory> findByNameIgnoreCase(String name);
}