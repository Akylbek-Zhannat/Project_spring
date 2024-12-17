package com.example.project;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;

@Controller
public class AuthController {

    private final CustomUserDetailsService customUserDetailsService;
    public AuthController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {
        user.setRole("USER");
        customUserDetailsService.saveUser(user);
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        User user = customUserDetailsService.getUserByUsername(principal.getName());
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/profile";
        } else {
            model.addAttribute("user", user);
            return "profile";
        }
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(Model model, Principal principal) {
        String username = principal.getName();
        User user = customUserDetailsService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "edit-profile";
    }


    @PostMapping("/profile/edit")
    public String updateProfile(User updatedUser, Principal principal) {
            User user = customUserDetailsService.getUserByUsername(principal.getName());
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setBirthday(updatedUser.getBirthday());
            customUserDetailsService.saveUser(user);
        return "redirect:/profile";
    }

    @GetMapping("/admin/profile")
    public String adminProfilePage(Model model, Principal principal) {
        User admin = customUserDetailsService.getUserByUsername(principal.getName());
        model.addAttribute("user", admin);
        return "admin-profile";
    }

}

