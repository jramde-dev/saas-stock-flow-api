package dev.jramde.saas.repository;

import dev.jramde.saas.entity.JrStockMvmt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrStockMvmtRepository extends JpaRepository<JrStockMvmt, String> {

    Page<JrStockMvmt> findAllByProductId(String productId, Pageable pageable);
}