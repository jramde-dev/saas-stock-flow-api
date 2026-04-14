package dev.jramde.saas.repository;

import dev.jramde.saas.entity.JrProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrProductRepository extends JpaRepository<JrProduct, String> {
}