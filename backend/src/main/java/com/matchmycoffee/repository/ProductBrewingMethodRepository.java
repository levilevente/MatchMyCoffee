package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.ProductBrewingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBrewingMethodRepository extends JpaRepository<ProductBrewingMethod, Long> {
}
