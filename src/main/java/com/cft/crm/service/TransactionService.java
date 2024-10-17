package com.cft.crm.service;

import com.cft.crm.entity.Seller;
import com.cft.crm.entity.Transaction;
import com.cft.crm.exception.DatabaseException;
import com.cft.crm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Получить список всех транзакций
    public List<Transaction> getAllTransactions() {
        try {
            return transactionRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при получении списка всех транзакций: " + e.getMessage());
        }
    }

    // Получить информацию о транзакции по ID
    public Optional<Transaction> getTransactionById(Long id) {
        return Optional.of(transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Транзакция с ID " + id + " не найдена")));
    }

    // Создать новую транзакцию
    public Transaction createTransaction(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при создании новой транзакции: " + e.getMessage());
        }
    }

    // Получить все транзакции продавца
    public List<Transaction> getTransactionsBySeller(Seller seller) {
        try {
            return transactionRepository.findBySeller(seller);
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при получении транзакций для продавца с ID " + seller.getId() + ": " + e.getMessage());
        }
    }

    // Получить все транзакции продавца за определенный период
    public List<Transaction> getTransactionsBySellerAndPeriod(Seller seller, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return transactionRepository.findBySellerAndTransactionDateBetween(seller, startDate, endDate);
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при получении транзакций продавца с ID " + seller.getId() + " за указанный период: " + e.getMessage());
        }
    }

    // Получить все транзакции за определенный период
    public List<Transaction> getTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return transactionRepository.findByTransactionDateBetween(startDate, endDate);
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при получении транзакций за указанный период: " + e.getMessage());
        }
    }
}
