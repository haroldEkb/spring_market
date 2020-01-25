package com.harold.spring_data_test.paypal;

import com.harold.spring_data_test.entities.Order;
import com.harold.spring_data_test.entities.OrderItem;
import com.harold.spring_data_test.services.OrderService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/paypal")
public class PayPalController {

    @Autowired
    private OrderService orderService;

    @Value("${paypal.clientId}")
    private String clientId;
    @Value("${paypal.clientSecret}")
    private String clientSecret;
    private String mode = "sandbox";
    private APIContext apiContext;

    @PostConstruct
    public void init(){
        apiContext = new APIContext(clientId, clientSecret, mode);
    }

    @GetMapping("/buy")
    public String buy(@RequestParam(name = "orderId") Long orderId, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:8080/app/paypal/cancel");
            redirectUrls.setReturnUrl("http://localhost:8080/app/paypal/success/" + orderId);

            Order order = orderService.findById(orderId);

            List<Transaction> transactions = new ArrayList<>();
            for (OrderItem orderItem : order.getItems()) {
                Amount amount = new Amount();
                amount.setCurrency("RUB");
                amount.setTotal(orderItem.getTotalPrice().toString());


                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setDescription(orderItem.getProduct().getTitle());
                transactions.add(transaction);
            }

            Payment payment = new Payment();
            payment.setPayer(payer);
            payment.setRedirectUrls(redirectUrls);
            payment.setTransactions(transactions);
            payment.setIntent("sale");

            Payment doPayment = payment.create(apiContext);

            Iterator<Links> links = doPayment.getLinks().iterator();

            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("message", "Вы сюда не должны были попасть...");
        return "paypal-result";
    }

    @GetMapping("/success/{orderId}")
    public String success(@PathVariable(name = "orderId") Long orderId, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            if (paymentId == null || paymentId.isEmpty() || payerId == null || payerId.isEmpty()) {
                return "redirect:/";
            }

            Payment payment = new Payment();
            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            if (executedPayment.getState().equals("approved")) {
                model.addAttribute("message", "Ваш заказ сформирован");
                orderService.setOrderStatus(orderId, Order.Status.PAID);
            } else {
                model.addAttribute("message", "Что-то пошло не так при формировании заказа, попробуйте повторить операцию");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "paypal-result";
    }

    @GetMapping("/cancel")
    public String cancel(Model model) {
        model.addAttribute("message", "Оплата заказа не была проведена. Возможно Вы отменили ее...");
        return "paypal-result";
    }
}
