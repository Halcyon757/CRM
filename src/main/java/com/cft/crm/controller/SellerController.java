package com.cft.crm.controller;

import com.cft.crm.entity.Seller;
import com.cft.crm.service.SellerService;
import com.cft.crm.service.SellerService.BestPeriodResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    // Получить список всех продавцов
    @GetMapping
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    // Получить информацию о продавце по ID
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        // Если продавец не найден, выбрасываем EntityNotFoundException
        Seller seller = sellerService.getSellerById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с ID " + id + " не найден"));
        return ResponseEntity.ok(seller);
    }

    // Создать нового продавца
    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller createdSeller = sellerService.createSeller(seller);
        return new ResponseEntity<>(createdSeller, HttpStatus.CREATED);
    }

    // Обновить информацию о продавце
    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id, @RequestBody Seller updatedSeller) {
        Seller updated = sellerService.updateSeller(id, updatedSeller)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с ID " + id + " не найден"));
        return ResponseEntity.ok(updated);
    }

    // Удалить продавца
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id); // Если продавец не найден, выбрасывается EntityNotFoundException
        return ResponseEntity.noContent().build();
    }

    // Получить самого продуктивного продавца за указанный период
    @GetMapping("/top-seller")
    public ResponseEntity<Seller> getTopSeller(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {

        Seller topSeller = sellerService.getTopSellerByPeriod(startDate, endDate)
                .orElseThrow(() -> new EntityNotFoundException("Самый продуктивный продавец за указанный период не найден"));
        return ResponseEntity.ok(topSeller);
    }

    // Получить список продавцов с общей суммой транзакций меньше указанного значения
    @GetMapping("/less-than")
    public List<Seller> getSellersWithTotalAmountLessThan(
            @RequestParam("amount") Double amount,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {

        return sellerService.getSellersWithTotalAmountLessThan(amount, startDate, endDate);
    }

    // Получить наилучший период транзакций для конкретного продавца
    @GetMapping("/{sellerId}/best-period")
    public ResponseEntity<BestPeriodResult> getBestTransactionPeriod(@PathVariable Long sellerId) {
        Seller seller = sellerService.getSellerById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с ID " + sellerId + " не найден"));

        BestPeriodResult bestPeriod = sellerService.getBestTransactionPeriod(seller)
                .orElseThrow(() -> new IllegalArgumentException("Не удалось найти наилучший период для продавца с ID " + sellerId));

        return ResponseEntity.ok(bestPeriod);
    }
}
