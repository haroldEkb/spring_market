package com.harold.spring_data_test.controllers;

import com.harold.spring_data_test.entities.User;
import com.harold.spring_data_test.services.UserService;
import com.harold.spring_data_test.utils.SystemTempUser;
import com.harold.spring_data_test.utils.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String showMyLoginPage(Model model) {
        model.addAttribute("systemUser", new SystemUser());
        return "registration-form";
    }

    @PostMapping("/process")
    public String processRegistrationForm(@Valid @ModelAttribute("systemUser") SystemUser systemUser, BindingResult bindingResult, Model model) {
        String phone = systemUser.getPhone();
        if (bindingResult.hasErrors()) {
            return "registration-form";
        }
//        User existing = userService.findByUsername(username);
        User existing = userService.findByPhone(phone);
        if (existing != null) {
            // theSystemUser.setUserName(null);
            model.addAttribute("systemUser", systemUser);
            model.addAttribute("registrationError", "User with current username is already exist");
            return "registration-form";
        }
        userService.save(systemUser);
        return "registration-confirmation";
    }

//    @RequestMapping("/process/anonymous")
//    public String processAnonymousRegistrationForm(@Valid @ModelAttribute("systemTempUser") SystemTempUser systemTempUser, BindingResult bindingResult, Model model) {
//        System.out.println("process anon");
//        String phone = systemTempUser.getPhone();
//        if (bindingResult.hasErrors()) {
//            return "redirect:/checkout";
//        }
////        User existing = userService.findByUsername(username);
//        User existing = userService.findByPhone(phone);
//        if (existing != null) {
//            // theSystemUser.setUserName(null);
//            model.addAttribute("systemTempUser", systemTempUser);
//            model.addAttribute("registrationError", "User with current phone is already exist");
//            return "redirect:/checkout";
//        }
//        userService.save(systemTempUser);
//        model.addAttribute("isRegistered", true);
//        return "forward:/orders/create/anonymous";
//    }


}
