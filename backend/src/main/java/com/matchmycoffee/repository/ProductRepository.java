package com.matchmycoffee.repository;

import com.matchmycoffee.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    @Query("SELECT p, COUNT(r), AVG(r.rating) " + "FROM Product p " + "LEFT JOIN p.reviews r " + "GROUP BY p")
    Page<Object[]> findAllWithReviewStats(Pageable pageable);

    @Query("SELECT p FROM Product p " + "LEFT JOIN FETCH p.productOrigins po " + "LEFT JOIN FETCH po.origin "
            + "LEFT JOIN FETCH p.productBrewingMethods pbm " + "LEFT JOIN FETCH pbm.brewingMethod "
            + "LEFT JOIN FETCH p.productTastes pt " + "LEFT JOIN FETCH pt.taste t " + "LEFT JOIN FETCH t.category "
            + "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT COUNT(r), AVG(r.rating) FROM Review r WHERE r.product.id = :id")
    List<Object[]> findProductStats(@Param("id") Long id);
}
