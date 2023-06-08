package com.example.md5api.repository;

import com.example.md5api.model.TheUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<TheUser, Long> {
    Optional<TheUser> findByUsername (String username);
    Boolean existsByUsername (String username);
    Boolean existsByEmail (String email);
}
