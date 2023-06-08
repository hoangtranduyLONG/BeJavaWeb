package com.example.md5api.repository;

import com.example.md5api.model.ERole;
import com.example.md5api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
