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
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public String showShop(Model model, HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                           @CookieValue(value = "page_size", required = false) Integer pageSize,
                           @CookieValue(value = "lastProducts", required = false) String lastProducts
    ) {
        ProductsFilter filter = new ProductsFilter(request);
        if (pageNumber == null || pageNumber < 1){
            pageNumber = 1;
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 10;
            response.addCookie(new Cookie("page_size", String.valueOf(pageSize)));
        }

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filters", filter.getFiltersBuilderString());
        model.addAttribute("categories", categoryService.findAll());

        if (lastProducts != null) {
            System.out.println("last products not null");
            List<Integer> lastViewedIds = Arrays.stream(lastProducts.split("l"))
                    .map(Integer::valueOf).collect(Collectors.toList());
            List<Product> lastViewed = productService.findByIds(lastViewedIds);
            System.out.println(lastViewedIds);
            model.addAttribute("lastViewed", lastViewed);
        } else {
            //Всегда идет сюда, почему-то куки не передаются, а в контроллере передаются.
            System.out.println("lastProducts are null / shop");
        }

        Page<Product> page = productService.findAllByPagingAndFiltering(filter.getSpecification(), PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "id"));
        model.addAttribute("page", page);
        return "shop";
    }

    @GetMapping("/product/{id}")
    public String showProduct(@PathVariable(name = "id") Integer id,
                              @CookieValue(value = "lastProducts", required = false) String lastProducts,
                              HttpServletResponse response, Model model
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
        model.addAttribute("product", product);
        return "product";
    }
}
