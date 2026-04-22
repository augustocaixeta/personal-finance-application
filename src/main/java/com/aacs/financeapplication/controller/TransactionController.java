package com.aacs.financeapplication.controller;

import com.aacs.financeapplication.model.Transaction;
import com.aacs.financeapplication.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("transactionsList", transactionService.findAll());
        return "transaction/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transaction/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("transaction") Transaction transaction,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "transaction/form";
        }
        transactionService.save(transaction);
        redirectAttributes.addFlashAttribute("successMessage", "Transação salva com sucesso!");
        return "redirect:/transaction";
    }
}
