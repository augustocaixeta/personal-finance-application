package com.aacs.financeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aacs.financeapplication.model.User;
import com.aacs.financeapplication.service.IUserService;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/registerUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes) {
        Integer id = userService.saveUser(user);
        String message = "Usuario '" + id + "' salvo com sucesso!";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/register";
    }

    @GetMapping("/accessDenied")
    public String getAccessDeniedPage() {
        return "user/accessDeniedPage";
    }
}
