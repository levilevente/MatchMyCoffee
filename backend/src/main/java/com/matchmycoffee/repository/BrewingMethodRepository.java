package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.BrewingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrewingMethodRepository extends JpaRepository<BrewingMethod, Long> {
}
