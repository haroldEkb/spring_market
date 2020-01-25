package com.harold.spring_data_test.repositories;

import com.harold.spring_data_test.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    public List<Product> findByCostGreaterThan(int minCost);
    public List<Product> findByCostLessThan(int maxCost);
    public List<Product> findByCostBetween(int minCost, int maxCost);

    public Page<Product> findByCostBetween(int minCost, int maxCost, Pageable pageable);
}
