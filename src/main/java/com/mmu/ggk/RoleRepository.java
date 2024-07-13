package com.mmu.ggk;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmu.ggk.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
