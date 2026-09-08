package gr.tastory.aueb.controller;

import gr.tastory.aueb.dto.RegisterDto;
import gr.tastory.aueb.model.Role;
import gr.tastory.aueb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute RegisterDto registerDto,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(
                registerDto.getEmail(),
                registerDto.getName(),
                registerDto.getPassword(),
                Role.CUSTOMER
        );
        return "redirect:/";
    }
}