package dev.jramde.saas.repository;

import dev.jramde.saas.entity.JrCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrCategoryRepository extends JpaRepository<JrCategory, String> {
}