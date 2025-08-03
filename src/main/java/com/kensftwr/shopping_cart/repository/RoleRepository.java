package com.kensftwr.shopping_cart.repository;

import com.kensftwr.shopping_cart.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}