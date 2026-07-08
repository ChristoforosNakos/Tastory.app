package gr.tastory.aueb.controller;

import gr.tastory.aueb.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MyOrdersController {

    private final OrderService orderService;

    public MyOrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model, Principal principal) {
        model.addAttribute("orders", orderService.findMyOrders(principal.getName()));
        return "my-orders";
    }
}