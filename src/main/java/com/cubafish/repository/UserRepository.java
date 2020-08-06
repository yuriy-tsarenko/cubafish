package com.cubafish.repository;

import com.cubafish.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String name);

    Boolean existsByEmail(String email);

    Boolean existsByUserContact(String contact);

    User findByUsername(String username);

    Boolean existsByUserRole(String role);
}
