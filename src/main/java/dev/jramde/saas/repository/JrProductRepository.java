package dev.jramde.saas.repository;

import dev.jramde.saas.entity.JrProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrProductRepository extends JpaRepository<JrProduct, String> {

    Optional<JrProduct> findByNameIgnoreCase(String name);

    Optional<JrProduct> findByReferenceIgnoreCase(String reference);

}