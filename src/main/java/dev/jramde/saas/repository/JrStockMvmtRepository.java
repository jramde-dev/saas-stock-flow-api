package dev.jramde.saas.repository;

import dev.jramde.saas.entity.JrStockMvmt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JrStockMvmtRepository extends JpaRepository<JrStockMvmt, String> {
}