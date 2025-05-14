package com.spartanwrath.repository;

import com.spartanwrath.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findByCategory(String category);
    @Query("SELECT p FROM Product p WHERE p.precio >= :from AND p.precio <= :to AND p.category = :category")
    List<Product> findByPrecioAndCategory(@Param("from") Integer from, @Param("to") Integer to, @Param("category") String category);

    @Query("SELECT p FROM Product p WHERE p.precio BETWEEN :from AND :to")
    List<Product> findByPrecioBetween(@Param("from") Integer from, @Param("to") Integer to);
}

