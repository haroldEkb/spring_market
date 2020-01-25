package com.harold.spring_data_test.services;

import com.harold.spring_data_test.entities.Product;
import com.harold.spring_data_test.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> getPageAll(int pageNumber, int pageSize){
        return getPageByCost(0, Integer.MAX_VALUE, pageNumber, pageSize);
    }

    public Page<Product> getPageByCost(Integer min, Integer max, int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return productRepository.findByCostBetween(
                min, max, PageRequest.of(pageNumber-1, pageSize, Sort.by(Sort.Direction.ASC, "id")));
    }

    public List<Product> findByCostGreaterThan(int min) {
        return productRepository.findByCostGreaterThan(min);
    }

    public List<Product> findByCost(Integer min, Integer max) {
        if (max >= min){
            return productRepository.findByCostBetween(min, max);
        } if (max == 0) return productRepository.findByCostBetween(min, Integer.MAX_VALUE);
        return findAll();
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    public Page<Product> findAllByPagingAndFiltering(Specification<Product> specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable);
    }

//    public void updateProduct(Integer id, Product product) {
//        Product productToUpdate = productRepository.getOne(id);
//        System.out.println(productToUpdate);
//        if (product.getTitle()!=null) productToUpdate.setTitle(product.getTitle());
//        if (product.getCost()!=null) productToUpdate.setCost(product.getCost());
//        System.out.println(productToUpdate);
//        productRepository.save(productToUpdate);
//    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    public List<Product> findByIds(Iterable<Integer> ids) {
        return productRepository.findAllById(ids);
    }
}
