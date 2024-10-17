package com.cft.crm.controller;

import com.cft.crm.entity.Seller;
import com.cft.crm.entity.Transaction;
import com.cft.crm.service.TransactionService;
import com.cft.crm.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SellerService sellerService;

    // Получить список всех транзакций
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    // Получить информацию о транзакции по ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        // Если транзакция не найдена, выбрасываем EntityNotFoundException
        Transaction transaction = transactionService.getTransactionById(id)
                .orElseThrow(() -> new EntityNotFoundException("Транзакция с ID " + id + " не найдена"));
        return ResponseEntity.ok(transaction);
    }

    // Создать новую транзакцию
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    // Получить все транзакции продавца по ID продавца
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Transaction>> getTransactionsBySeller(@PathVariable Long sellerId) {
        Seller seller = sellerService.getSellerById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с ID " + sellerId + " не найден"));

        List<Transaction> transactions = transactionService.getTransactionsBySeller(seller);
        return ResponseEntity.ok(transactions);
    }

    // Получить транзакции продавца за определенный период
    @GetMapping("/seller/{sellerId}/period")
    public ResponseEntity<List<Transaction>> getTransactionsBySellerAndPeriod(
            @PathVariable Long sellerId,
            @RequestParam("start") LocalDateTime startDate,
            @RequestParam("end") LocalDateTime endDate) {

        Seller seller = sellerService.getSellerById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с ID " + sellerId + " не найден"));

        List<Transaction> transactions = transactionService.getTransactionsBySellerAndPeriod(seller, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}
