package com.kensftwr.shopping_cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kensftwr.shopping_cart.models.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

}
