package com.harold.spring_data_test.services;


import com.harold.spring_data_test.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailMessageBuilder {
    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildOrderEmail(Order order) {
        Context context = new Context();
        context.setVariable("order", order);
        return templateEngine.process("order-mail", context);
    }
}
