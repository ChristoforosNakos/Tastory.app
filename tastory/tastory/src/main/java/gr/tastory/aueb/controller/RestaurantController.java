package gr.tastory.aueb.controller;


import gr.tastory.aueb.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // Λίστα όλων των εστιατορίων (δημόσια)
    @GetMapping("/restaurants")
    public String listRestaurants(Model model) {
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "restaurants";
    }

    // Φόρμα δημιουργίας (μόνο για owners)
    @GetMapping("/restaurants/new")
    public String showCreateForm() {
        return "restaurant-form";
    }

    // Επεξεργασία δημιουργίας (μόνο για owners)
    @PostMapping("/restaurants/new")
    public String createRestaurant(@RequestParam String name,
                                   @RequestParam String address,
                                   Principal principal) {
        restaurantService.createRestaurant(name, address, principal.getName());
        return "redirect:/restaurants";
    }
}
