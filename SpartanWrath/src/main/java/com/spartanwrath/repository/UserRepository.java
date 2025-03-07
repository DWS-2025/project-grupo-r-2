package com.spartanwrath.repository;

import com.spartanwrath.model.Product;
import com.spartanwrath.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    Optional<User> findById(long id);

    Optional<User> findByName(String name);

    Optional<User> findByUsername(String name);

    boolean existsByUsername(String username);
}