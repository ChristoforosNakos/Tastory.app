package gr.tastory.aueb.controller;

import gr.tastory.aueb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin-users";
    }

    @PostMapping("/users/{id}/promote")
    public String promoteUser(@PathVariable Long id) {
        userService.promoteToOwner(id);
        return "redirect:/admin/users";
    }
}


