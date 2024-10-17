package com.cft.crm.service;

import com.cft.crm.entity.Seller;
import com.cft.crm.entity.Transaction;
import com.cft.crm.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

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
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    // Тест получения транзакции по ID
    @Test
    void testGetTransactionById() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = transactionService.getTransactionById(1L);
        assertTrue(result.isPresent());
        assertEquals(transaction, result.get());
    }

    // Тест создания новой транзакции
    @Test
    void testCreateTransaction() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);
        assertEquals(transaction, createdTransaction);
    }

    // Тест получения всех транзакций по продавцу
    @Test
    void testGetTransactionsBySeller() {
        when(transactionRepository.findBySeller(seller)).thenReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionService.getTransactionsBySeller(seller);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    // Тест получения всех транзакций продавца за определенный период
    @Test
    void testGetTransactionsBySellerAndPeriod() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        when(transactionRepository.findBySellerAndTransactionDateBetween(seller, startDate, endDate))
                .thenReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionService.getTransactionsBySellerAndPeriod(seller, startDate, endDate);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }

    // Тест получения всех транзакций за определенный период
    @Test
    void testGetTransactionsByPeriod() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        when(transactionRepository.findByTransactionDateBetween(startDate, endDate))
                .thenReturn(Arrays.asList(transaction));

        List<Transaction> transactions = transactionService.getTransactionsByPeriod(startDate, endDate);
        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(0));
    }
}
