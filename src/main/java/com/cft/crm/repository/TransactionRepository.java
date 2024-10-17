package com.cft.crm.repository;

import com.cft.crm.entity.Transaction;
import com.cft.crm.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Получить все транзакции для указанного продавца
    List<Transaction> findBySeller(Seller seller);

    // Получить все транзакции продавца в указанном периоде
    List<Transaction> findBySellerAndTransactionDateBetween(Seller seller, LocalDateTime startDate, LocalDateTime endDate);

    // Получить все транзакции за определенный период времени
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Найти сумму всех транзакций для указанного продавца за период
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.seller = :seller AND t.transactionDate BETWEEN :startDate AND :endDate")
    Double findTotalAmountBySellerAndPeriod(Seller seller, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t.seller FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate GROUP BY t.seller HAVING SUM(t.amount) < :amount")
    List<Seller> findSellersWithTotalAmountLessThan(Double amount, LocalDateTime startDate, LocalDateTime endDate);

    // Получить все транзакции продавца, отсортированные по дате
    List<Transaction> findBySellerOrderByTransactionDateAsc(Seller seller);
}
