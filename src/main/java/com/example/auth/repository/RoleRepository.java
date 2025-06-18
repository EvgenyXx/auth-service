package com.example.auth.repository;

import com.example.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);

    Set<Role>findByNameIn(Collection<String>name);
}
