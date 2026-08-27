package gr.tastory.aueb.controller;

import gr.tastory.aueb.dto.RegisterDto;
import gr.tastory.aueb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String processRegister(@ModelAttribute RegisterDto registerDto) {
        userService.register(
                registerDto.getEmail(),
                registerDto.getName(),
                registerDto.getPassword(),
                registerDto.getRole()
        );
        return "redirect:/";
    }
}