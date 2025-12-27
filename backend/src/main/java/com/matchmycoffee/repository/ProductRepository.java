package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.repository.exception.RepositoryException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    Page<Product> findAll(Pageable pageable);

    Product findByName(String name) throws RepositoryException;
}
