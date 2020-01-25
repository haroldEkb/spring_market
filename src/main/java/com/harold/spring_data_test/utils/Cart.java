package com.harold.spring_data_test.utils;

import com.harold.spring_data_test.entities.Order;
import com.harold.spring_data_test.entities.OrderItem;
import com.harold.spring_data_test.entities.Product;
import com.harold.spring_data_test.errors_handlers.ResourseNotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private Map<Integer, OrderItem> items;
    private Double totalPrice;

    public Map<Integer, OrderItem> getItems() {
        return items;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    @PostConstruct
    public void init() {
        items = new LinkedHashMap<>();
    }

    public void addProduct(Product product) {
        OrderItem item = items.get(product.getId());
        if (item == null) {
            item = new OrderItem();
            item.setItemPrice(product.getCost().doubleValue());
            item.setProduct(product);
            item.setQuantity(0);
        }
        item.setQuantity(item.getQuantity() + 1);
        item.setTotalPrice(item.getItemPrice() * item.getQuantity());
        items.put(product.getId(), item);
        recalculate();
    }

    public void removeItem(Product product) {
        items.remove(product.getId());
        recalculate();
    }

    public void clear() {
        items.clear();
        totalPrice = 0.0;
    }

    private void recalculate() {
        totalPrice = 0.0;
        items.values().forEach(oi -> totalPrice += oi.getTotalPrice());
    }



//    private Map<Product, Integer> productMap;
//    private Integer totalPrice;

//    @PostConstruct
//    public void init() {
//        products = new ArrayList<>();
//        productMap = new HashMap<>();
//        totalPrice = 0;
//    }
//
//    public void addProduct(Product product) {
//        products.add(product);
//        totalPrice += product.getCost();
//        if (productMap.containsKey(product)){
////            productMap.replace(product, productMap.get(product)+1);
////            productMap.computeIfPresent(product, (k,v)->v+1);
//            productMap.merge(product, 1, Integer::sum);
//        } else {
//            productMap.put(product, 1);
//        }
//    }
//
//    public void removeProduct(Product product){
//        totalPrice -= product.getCost();
//        products.remove(product);
//
//        Integer count = productMap.get(product);
//        if (count > 1){
//            productMap.replace(product, count - 1);
//        } else {
//            productMap.remove(product);
//        }
//    }
//
//    public Integer getTotalPrice() {
//        return totalPrice;
//    }
//
//    public Map<Product, Integer> getProductMap() {
//        return productMap;
//    }
//
//    @Override
//    public String toString() {
//        for (Map.Entry<Product, Integer> entry : productMap.entrySet()) {
//            System.out.println(entry.getKey() + " День недели = " + entry.getValue());
//        }
//        return "";
//    }
}
