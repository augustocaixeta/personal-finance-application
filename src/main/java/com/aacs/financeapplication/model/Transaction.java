package com.aacs.financeapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 3, max = 100, message = "A descrição deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String description;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotBlank(message = "O tipo é obrigatório")
    @Column(nullable = false, length = 20)
    private String type;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(min = 2, max = 50, message = "A categoria deve ter entre 2 e 50 caracteres")
    @Column(nullable = false, length = 50)
    private String category;

    @NotNull(message = "A data é obrigatória")
    @Column(nullable = false)
    private LocalDate date;

    public Transaction() {
    }

    public Transaction(String description, BigDecimal amount, String type, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }
}
