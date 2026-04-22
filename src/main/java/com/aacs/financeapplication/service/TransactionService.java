package com.aacs.financeapplication.service;

import com.aacs.financeapplication.model.Transaction;
import com.aacs.financeapplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAllByOrderByDateDesc();
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> findByType(String type) {
        return transactionRepository.findByType(type);
    }

    public List<Transaction> findByCategory(String category) {
        return transactionRepository.findByCategory(category);
    }
}
