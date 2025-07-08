package com.kensftwr.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kensftwr.shopping_cart.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
