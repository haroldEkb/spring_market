package com.harold.spring_data_test.repositories;

import com.harold.spring_data_test.entities.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAllByUser_id(Integer userId);
}
