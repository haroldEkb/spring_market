package com.harold.spring_data_test.controllers;

import com.harold.spring_data_test.entities.Order;
import com.harold.spring_data_test.entities.OrderItem;
import com.harold.spring_data_test.entities.Product;
import com.harold.spring_data_test.entities.User;
import com.harold.spring_data_test.errors_handlers.ResourseNotFoundException;
import com.harold.spring_data_test.services.UserService;
import com.harold.spring_data_test.utils.Cart;
import com.harold.spring_data_test.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/cart")
public class CartController {
    private ProductService productsService;
    private UserService userService;
    private Cart cart;

    @Autowired
    public void setProductsService(ProductService productsService) {
        this.productsService = productsService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @GetMapping("")
    public String show(Model model, Principal principal) {
        if (principal != null){
            User user = userService.findByPhone(principal.getName());
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("firstName", user.getFirstName());
        }
        model.addAttribute("items", cart.getItems().values());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        return "cart";
    }

    @GetMapping("/add")
    public void addProductToCart(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = productsService.findById(id)
                .orElseThrow(() -> new ResourseNotFoundException("Product not found: " + id));
        cart.addProduct(product);
        response.sendRedirect(request.getHeader("referer"));
    }
}

