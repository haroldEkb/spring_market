package com.harold.spring_data_test.repositories;

import com.harold.spring_data_test.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findOneByPhone(String phone);
    boolean existsByPhone(String phone);
}
