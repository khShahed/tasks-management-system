package com.shahed.taskmanagementsystem.jpa.repository;

import com.shahed.taskmanagementsystem.jpa.entity.ERole;
import com.shahed.taskmanagementsystem.jpa.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
