package com.harold.spring_data_test.services;

import com.harold.spring_data_test.entities.Order;
import com.harold.spring_data_test.entities.User;
import com.harold.spring_data_test.repositories.OrderRepository;
import com.harold.spring_data_test.utils.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    private Cart cart;

    @Autowired
    private void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public OrderService(OrderRepository orderRepository, Cart cart) {
        this.orderRepository = orderRepository;
        this.cart = cart;
    }

    public Order createOrder(User user, String phone, String address) {
        Order order = new Order(user, phone, address);
        cart.getItems().values().stream().forEach(i -> order.addItem(i));
        cart.clear();
        return orderRepository.save(order);
    }

    public Map<String, Double> getOrderInfo(){
        Map<String, Double> orderInfo = new HashMap<>();
        orderInfo.put("itemCount", new Double(cart.getItems().size()));
        orderInfo.put("totalPrice", cart.getTotalPrice());
        return orderInfo;
    }

    public List<Order> findAllByUserId(Integer userId){
        return orderRepository.findAllByUser_id(userId);
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
    }

    public void setOrderStatus(Long orderId, Order.Status status){
        Order order = orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        order.setStatus(status);
        orderRepository.save(order);
    }
}
