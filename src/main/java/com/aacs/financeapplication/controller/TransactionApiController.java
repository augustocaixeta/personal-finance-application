package com.aacs.financeapplication.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aacs.financeapplication.model.Transaction;
import com.aacs.financeapplication.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionApiController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> listar() {
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> buscarPorId(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);

        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Transaction> criar(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> atualizar(@PathVariable Long id, @RequestBody Transaction transaction) {
        Optional<Transaction> existingTransaction = transactionService.findById(id);

        if (existingTransaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        transaction.setId(id);
        Transaction savedTransaction = transactionService.save(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.findById(id);

        if (transaction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        transactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
