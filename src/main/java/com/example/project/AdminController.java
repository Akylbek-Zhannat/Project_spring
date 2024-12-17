package com.example.project;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin/users")
public class AdminController {

    private final CustomUserDetailsService customUserDetailsService;

    public AdminController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", customUserDetailsService.getAllUsers());
        return "user-list";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable String id, Model model) {
        User user = customUserDetailsService.getUserById(id);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable String id, @ModelAttribute User updatedUser,
                           @RequestParam(required = false) String newPassword) {
        customUserDetailsService.updateUser(id, updatedUser, newPassword);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        customUserDetailsService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User newUser) {
        customUserDetailsService.saveUser(newUser);
        return "redirect:/admin/users";
    }

}
