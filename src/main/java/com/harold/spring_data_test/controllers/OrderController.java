package com.harold.spring_data_test.controllers;

import com.harold.spring_data_test.entities.Order;
import com.harold.spring_data_test.entities.User;
import com.harold.spring_data_test.services.MailService;
import com.harold.spring_data_test.services.OrderService;
import com.harold.spring_data_test.services.UserService;
import com.harold.spring_data_test.utils.SystemTempUser;
import com.harold.spring_data_test.utils.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.management.VMOptionCompositeData;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal){
        User user = null;
        if (principal != null){
            user = userService.findByPhone(principal.getName());
//            model.addAttribute("user", user);
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("firstName", user.getFirstName());
        } else {
            model.addAttribute("systemTempUser", new SystemTempUser());
        }
        model.addAttribute("itemCount", orderService.getOrderInfo().get("itemCount"));
        model.addAttribute("totalPrice", orderService.getOrderInfo().get("totalPrice"));

        return "checkout";
    }

    @GetMapping("/create")
    public String createOrder(Principal principal, Model model,
                              @RequestParam(name = "phone") String phone,
                              @RequestParam(name = "firstName") String firstName,
                              @RequestParam(name = "address") String address) {
        User user = null;
        System.out.println(phone);
        System.out.println(firstName);
        System.out.println(address);
        if (principal != null) {
            user = userService.findByPhone(principal.getName());
        } else {
            if (userService.isUserExists(phone)) {
                user = userService.findByPhone(phone);
            } else {
                SystemUser systemUser = new SystemUser();
                systemUser.setPhone(phone);
                systemUser.setFirstName(firstName);
                user = userService.save(systemUser);
            }
        }
        System.out.println(user.getId());
        Order order = orderService.createOrder(user, phone, address);
//        if (user.getEmail() != null) {
//            mailService.sendOrderMail(order);
//        }
        model.addAttribute("orderId", order.getId());
        return "order-confirmed";
    }

    @GetMapping("/history")
    public String history(Model model, Principal principal){
        List<Order> orders = orderService.findAllByUserId(userService.findByPhone(principal.getName()).getId());
        for (Order order : orders) {
            System.out.println(order.getUser());
        }
        model.addAttribute("orders", orders);
        return "history";
    }

//    @PostMapping("/anonymous")
//    public String createAnonymousOrder(@RequestParam(name = "systemTempUser") SystemUser user
////            ,@ModelAttribute(name = "isRegistered") Boolean isRegistered
//    ) {
//        System.out.println("lol");
//        System.out.println(user.getPhone());
//        if (true){
//            System.out.println("registered == null");
//            return "forward:/register/process/anonymous";
//        } else {
////            Order order = orderService.createOrder(userService.findByPhone(user.getPhone()));
////            mailService.sendOrderMail(order);
//            return "redirect:/shop";
//        }
//    }
}
