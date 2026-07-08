package gr.tastory.aueb.controller;

import gr.tastory.aueb.model.OrderStatus;
import org.springframework.ui.Model;
import gr.tastory.aueb.dto.OrderFormDto;
import gr.tastory.aueb.model.Order;
import gr.tastory.aueb.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/restaurants/{restaurantId}/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String placeOrder(@PathVariable Long restaurantId,
                             @ModelAttribute OrderFormDto form,
                             Principal principal) {
        try {
            orderService.placeOrder(restaurantId, form.getQuantities(), principal.getName());
            return "redirect:/restaurants/" + restaurantId + "/products?ordered";
        } catch (IllegalArgumentException e) {
            return "redirect:/restaurants/" + restaurantId + "/products?error";
        }
    }

    @GetMapping
    public String listOrders(@PathVariable Long restaurantId,
                             Model model,
                             Principal principal) {

        List<Order> orders = orderService.findOrdersForRestaurant(restaurantId, principal.getName());

        model.addAttribute("orders", orders);
        model.addAttribute("restaurantId", restaurantId);
        return "orders";
    }

    @PostMapping("/{orderId}/status")
    public String updateStatus(@PathVariable Long restaurantId,
                               @PathVariable Long orderId,
                               @RequestParam OrderStatus status,
                               Principal principal) {
        try {
            orderService.updateStatus(restaurantId, orderId, status, principal.getName());
            return "redirect:/restaurants/" + restaurantId + "/orders";
        } catch (IllegalStateException e) {
            return "redirect:/restaurants/" + restaurantId + "/orders?statusError";
        }
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleSecurity() {
        return "error-403";
    }



}