package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.ProductTaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTasteRepository extends JpaRepository<ProductTaste, Long> {
}
