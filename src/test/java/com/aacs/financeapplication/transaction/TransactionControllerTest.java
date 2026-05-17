package com.aacs.financeapplication.transaction;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.aacs.financeapplication.config.TestConfig;
import com.aacs.financeapplication.config.SecurityConfig;
import com.aacs.financeapplication.controller.TransactionController;
import com.aacs.financeapplication.model.Transaction;
import com.aacs.financeapplication.service.TransactionService;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc
@Import({ TestConfig.class, SecurityConfig.class })
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @AfterEach
    void resetMocks() {
        reset(transactionService);
    }

    private Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Transacao teste");
        transaction.setAmount(new BigDecimal("65.24"));
        transaction.setType("RECEITA");
        transaction.setCategory("Outros");
        transaction.setDate(LocalDate.of(2026, 5, 17));
        return transaction;
    }

    @Test
    @DisplayName("GET /transaction - Redireciona usuario nao autenticado para login")
    void testIndexNotAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/transaction"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("GET /transaction - Lista transacoes para usuario autenticado")
    @WithMockUser(username = "usuario@finance.com.br", authorities = { "User" })
    void testIndexAuthenticatedUser() throws Exception {
        when(transactionService.findAll()).thenReturn(List.of(createTransaction()));

        mockMvc.perform(get("/transaction")
                .with(testSecurityContext()))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/index"))
                .andExpect(model().attributeExists("transactionsList"))
                .andExpect(content().string(containsString("Transacao teste")))
                .andExpect(content().string(not(containsString("href=\"/transaction/create\""))));
    }

    @Test
    @DisplayName("GET /transaction/create - Exibe formulario para usuario admin")
    @WithMockUser(username = "admin@finance.com.br", authorities = { "Admin" })
    void testCreateFormAuthorizedUser() throws Exception {
        mockMvc.perform(get("/transaction/create")
                .with(testSecurityContext()))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/form"))
                .andExpect(model().attributeExists("transaction"))
                .andExpect(content().string(containsString("Cadastrar")));
    }

    @Test
    @DisplayName("GET /transaction/create - Bloqueia usuario sem permissao admin")
    @WithMockUser(username = "usuario@finance.com.br", authorities = { "User" })
    void testCreateFormNotAuthorizedUser() throws Exception {
        mockMvc.perform(get("/transaction/create")
                .with(testSecurityContext()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /transaction/save - Falha na validacao e retorna para o formulario")
    @WithMockUser(username = "admin@finance.com.br", authorities = { "Admin" })
    void testSaveTransactionValidationError() throws Exception {
        Transaction transaction = new Transaction();

        mockMvc.perform(post("/transaction/save")
                .with(testSecurityContext())
                .with(csrf())
                .flashAttr("transaction", transaction))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/form"))
                .andExpect(model().attributeHasErrors("transaction"));

        verify(transactionService, never()).save(any(Transaction.class));
    }

    @Test
    @DisplayName("POST /transaction/save - Transacao valida e salva com sucesso")
    @WithMockUser(username = "admin@finance.com.br", authorities = { "Admin" })
    void testSaveValidTransaction() throws Exception {
        Transaction transaction = createTransaction();

        mockMvc.perform(post("/transaction/save")
                .with(testSecurityContext())
                .with(csrf())
                .flashAttr("transaction", transaction))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transaction"));

        verify(transactionService).save(any(Transaction.class));
    }
}
