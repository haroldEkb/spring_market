package com.harold.spring_data_test.controllers;

import com.harold.spring_data_test.entities.Product;
import com.harold.spring_data_test.errors_handlers.ResourseNotFoundException;
import com.harold.spring_data_test.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class RestProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public List<Product> findAllProducts(){
        return productService.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product){
        product.setId(null);
        return productService.save(product);
    }

    @GetMapping("/{id}")
    public Product findProductById(@PathVariable(name = "id") Integer id){
        return productService.findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("Product not found: " + id));
    }

    @PutMapping("/")
    public Product updateProduct(@RequestBody Product product){
        return productService.save(product);
    }

    @PutMapping("/{id}")
    public Product saveOrUpdateProduct(@RequestBody Product newProduct,
                                 @PathVariable(name = "id") Integer id){
        return productService.findById(id)
                .map(product -> {
                    product.setTitle(newProduct.getTitle());
                    product.setCost(newProduct.getCost());
                    return productService.save(product);
                })
                .orElseGet(() -> {
                    newProduct.setId(id);
                    return productService.save(newProduct);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable(name = "id") Integer id){
        productService.deleteById(id);
    }
}
