package com.harold.spring_data_test.utils;

import com.harold.spring_data_test.entities.Product;
import com.harold.spring_data_test.repositories.specifications.ProductSpecifications;
import org.springframework.data.jpa.domain.Specification;

import javax.servlet.http.HttpServletRequest;

public class ProductsFilter {
    private Specification<Product> specification;
    private StringBuilder filtersBuilder;

    public ProductsFilter(HttpServletRequest request){
        filtersBuilder = new StringBuilder();
        specification = Specification.where(null);

        if (request.getParameter("word") != null && !request.getParameter("word").isEmpty()) {
            specification = specification.and(ProductSpecifications.titleContains(request.getParameter("word")));
            filtersBuilder.append("&word=" + request.getParameter("word"));
        }

        if (request.getParameter("min") != null && !request.getParameter("min").isEmpty()) {
            specification = specification.and(ProductSpecifications.priceGreaterThanOrEq(Double.parseDouble(request.getParameter("min"))));
            filtersBuilder.append("&min=" + request.getParameter("min"));
        }

        if (request.getParameter("max") != null && !request.getParameter("max").isEmpty()) {
            specification = specification.and(ProductSpecifications.priceLesserThanOrEq(Double.parseDouble(request.getParameter("max"))));
            filtersBuilder.append("&max=" + request.getParameter("max"));
        }

        if (request.getParameter("categoryId") != null && !request.getParameter("categoryId").isEmpty()) {
            specification = specification.and(ProductSpecifications.categoryEq(Integer.valueOf(request.getParameter("categoryId"))));
            filtersBuilder.append("&categoryId=" + request.getParameter("categoryId"));
        }
    }

    public Specification<Product> getSpecification() {
        return specification;
    }

    public String getFiltersBuilderString() {
        return filtersBuilder.toString();
    }
}
