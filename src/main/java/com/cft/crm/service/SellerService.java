package com.cft.crm.service;

import com.cft.crm.entity.Seller;
import com.cft.crm.entity.Transaction;
import com.cft.crm.exception.DatabaseException;
import com.cft.crm.repository.SellerRepository;
import com.cft.crm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

// import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Получить список всех продавцов
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    // Получить информацию о продавце по ID
    public Optional<Seller> getSellerById(Long id) {
        return Optional.of(sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с ID " + id + " не найден")));
    }

    // Создать нового продавца
    public Seller createSeller(Seller seller) {
        try {
            return sellerRepository.save(seller);
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при создании нового продавца: " + e.getMessage());
        }
    }

    // Обновить информацию о продавце
    public Optional<Seller> updateSeller(Long id, Seller updatedSeller) {
        return Optional.of(sellerRepository.findById(id)
                .map(seller -> {
                    seller.setName(updatedSeller.getName());
                    seller.setContactInfo(updatedSeller.getContactInfo());
                    seller.setRegistrationDate(updatedSeller.getRegistrationDate());
                    try {
                        return sellerRepository.save(seller);
                    } catch (Exception e) {
                        throw new DatabaseException("Ошибка при обновлении продавца с ID " + id + ": " + e.getMessage());
                    }
                })
                .orElseThrow(() -> new EntityNotFoundException("Продавец с ID " + id + " не найден")));
    }

    // Удалить продавца
    public boolean deleteSeller(Long id) {
        if (sellerRepository.existsById(id)) {
            try {
                sellerRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new DatabaseException("Ошибка при удалении продавца с ID " + id + ": " + e.getMessage());
            }
        } else {
            throw new EntityNotFoundException("Продавец с ID " + id + " не найден");
        }
    }

    // Получить самого продуктивного продавца за указанный период
    public Optional<Seller> getTopSellerByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Seller> sellers = sellerRepository.findAll();

        return sellers.stream()
                .max(Comparator.comparing(seller -> {
                    Double totalAmount = transactionRepository.findTotalAmountBySellerAndPeriod(seller, startDate, endDate);
                    return totalAmount != null ? totalAmount : 0.0;
                }))
                .or(() -> {
                    throw new EntityNotFoundException("Самый продуктивный продавец за указанный период не найден");
                });
    }

    // Получить список продавцов с общей суммой транзакций меньше указанного значения за указанный период
    public List<Seller> getSellersWithTotalAmountLessThan(Double amount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return transactionRepository.findSellersWithTotalAmountLessThan(amount, startDate, endDate);
        } catch (Exception e) {
            throw new DatabaseException("Ошибка при получении продавцов с общей суммой транзакций меньше " + amount + ": " + e.getMessage());
        }
    }

    // Алгоритм для нахождения наилучшего периода времени
    public Optional<BestPeriodResult> getBestTransactionPeriod(Seller seller) {
        List<Transaction> transactions = transactionRepository.findBySellerOrderByTransactionDateAsc(seller);
        if (transactions.isEmpty()) {
            throw new EntityNotFoundException("Для продавца с ID " + seller.getId() + " не найдено транзакций");
        }

        int maxTransactionCount = 0;
        LocalDateTime bestStartDate = null;
        LocalDateTime bestEndDate = null;

        // Алгоритм скользящего окна
        for (int start = 0; start < transactions.size(); start++) {
            for (int end = start; end < transactions.size(); end++) {
                // Получаем диапазон
                LocalDateTime startDate = transactions.get(start).getTransactionDate();
                LocalDateTime endDate = transactions.get(end).getTransactionDate();

                int transactionCount = end - start + 1;

                // Если нашли больше транзакций, чем ранее, обновляем результат
                if (transactionCount > maxTransactionCount) {
                    maxTransactionCount = transactionCount;
                    bestStartDate = startDate;
                    bestEndDate = endDate;
                }
            }
        }

        // Возвращаем наилучший период и количество транзакций
        return Optional.of(new BestPeriodResult(seller, bestStartDate, bestEndDate, maxTransactionCount));
    }

    // Вспомогательный класс для хранения результата
    public static class BestPeriodResult {
        private Long sellerId;
        private String sellerName;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private int transactionCount;

        public BestPeriodResult(Seller seller, LocalDateTime startDate, LocalDateTime endDate, int transactionCount) {
            this.sellerId = seller.getId();
            this.sellerName = seller.getName();
            this.startDate = startDate;
            this.endDate = endDate;
            this.transactionCount = transactionCount;
        }

        public Long getSellerId() {
            return sellerId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public int getTransactionCount() {
            return transactionCount;
        }
    }
}
