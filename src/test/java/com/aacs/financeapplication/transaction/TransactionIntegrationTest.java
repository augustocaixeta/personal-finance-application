package com.aacs.financeapplication.transaction;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.aacs.financeapplication.model.Transaction;
import com.aacs.financeapplication.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testSaveTransactionIntegration() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setDescription("Transacao teste");
        transaction.setAmount(new BigDecimal("65.24"));
        transaction.setType("RECEITA");
        transaction.setCategory("Outros");
        transaction.setDate(LocalDate.now());

        mockMvc.perform(post("/transaction/save")
                .with(user("admin").authorities(new SimpleGrantedAuthority("Admin")))
                .with(csrf())
                .flashAttr("transaction", transaction))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transaction"));

        assertTrue(transactionRepository.findAll()
                .stream()
                .anyMatch(t -> "Transacao teste".equals(t.getDescription())));
    }
}
