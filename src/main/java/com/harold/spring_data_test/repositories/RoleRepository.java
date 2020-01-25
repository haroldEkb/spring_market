package com.harold.spring_data_test.repositories;

import com.harold.spring_data_test.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findOneByName(String roleName);
}
