package com.harold.spring_data_test.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index(Model model){
        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/authenticateTheUser")
    public String auth(){
        return "/shop";
    }

}
