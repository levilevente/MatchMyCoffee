package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.ProductOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOriginRepository extends JpaRepository<ProductOrigin, Long> {
}
