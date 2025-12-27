package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.Taste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasteRepository extends JpaRepository<Taste, Long> {
}
