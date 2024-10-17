package com.cft.crm.controller;

import com.cft.crm.entity.Seller;
import com.cft.crm.entity.Transaction;
import com.cft.crm.service.SellerService;
import com.cft.crm.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private TransactionController transactionController;

    private Seller seller;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Тестовый продавец и транзакция
        seller = new Seller();
        seller.setId(1L);
        seller.setName("Alice Cooper");

        transaction = new Transaction(seller, new BigDecimal("100.0"), "CASH", LocalDateTime.of(2024, 1, 1, 10, 0));
    }

    // Тест получения всех транзакций
    @Test
    void testGetAllTransactions() {
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionController.getAllTransactions();
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    // Тест получения транзакции по ID
    @Test
    void testGetTransactionById() {
        when(transactionService.getTransactionById(1L)).thenReturn(Optional.of(transaction));

        ResponseEntity<Transaction> response = transactionController.getTransactionById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    // Тест создания новой транзакции
    @Test
    void testCreateTransaction() {
        when(transactionService.createTransaction(transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    // Тест получения транзакций по продавцу
    @Test
    void testGetTransactionsBySeller() {
        when(sellerService.getSellerById(1L)).thenReturn(Optional.of(seller));
        when(transactionService.getTransactionsBySeller(seller)).thenReturn(Arrays.asList(transaction));

        ResponseEntity<List<Transaction>> response = transactionController.getTransactionsBySeller(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(transaction, response.getBody().get(0));
    }

    // Тест получения транзакций продавца за период
    @Test
    void testGetTransactionsBySellerAndPeriod() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        when(sellerService.getSellerById(1L)).thenReturn(Optional.of(seller));
        when(transactionService.getTransactionsBySellerAndPeriod(seller, startDate, endDate))
                .thenReturn(Arrays.asList(transaction));

        ResponseEntity<List<Transaction>> response = transactionController.getTransactionsBySellerAndPeriod(1L, startDate, endDate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(transaction, response.getBody().get(0));
    }
}
