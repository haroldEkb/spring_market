package com.harold.spring_data_test.controllers;

import com.harold.spring_data_test.entities.Category;
import com.harold.spring_data_test.entities.Product;
import com.harold.spring_data_test.services.CategoryService;
import com.harold.spring_data_test.services.ProductService;
import com.harold.spring_data_test.utils.ProductsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Product showProduct(@PathVariable(name = "id") Integer id,
                               @CookieValue(value = "lastProducts", required = false) String lastProducts,
                               HttpServletResponse response
    ){
        Product product = productService.findById(id).orElseThrow(NoSuchElementException::new);
        if (lastProducts == null) {
            System.out.println("lastProducts are null / ProdController");
            lastProducts = String.valueOf(product.getId());
            response.addCookie(new Cookie("lastProducts", lastProducts));
        } else {
            lastProducts = lastProducts + "l" + product.getId();
            response.addCookie(new Cookie("lastProducts", lastProducts));
        }
        System.out.println(lastProducts);
        return product;
    }



    @GetMapping("/edit")
    public String showEditPage(@RequestParam(name = "id", required = false) Integer id, Model model){
        Product product = null;
        if (id != null) {
            product = productService.findById(id).orElseGet(Product::new);
            model.addAttribute("isModifying", true);
        }
        else {
            product = new Product();
            model.addAttribute("isModifying", false);
        }
        model.addAttribute("product", product);
        return "edit";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute(name = "editProduct") Product product){
        productService.save(product);
        return "redirect:/products/";
    }
}
