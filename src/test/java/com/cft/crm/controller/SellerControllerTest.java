package com.cft.crm.controller;

import com.cft.crm.entity.Seller;
import com.cft.crm.service.SellerService;
import com.cft.crm.service.SellerService.BestPeriodResult;
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

class SellerControllerTest {

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private SellerController sellerController;

    private Seller seller;
    private BestPeriodResult bestPeriodResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создаем тестового продавца
        seller = new Seller();
        seller.setId(1L);
        seller.setName("Alice Cooper");

        // Создаем результат лучшего периода
        bestPeriodResult = new BestPeriodResult(seller, LocalDateTime.of(2024, 1, 1, 10, 0),
                LocalDateTime.of(2024, 1, 3, 14, 0), 3);
    }

    // Тест получения всех продавцов
    @Test
    void testGetAllSellers() {
        when(sellerService.getAllSellers()).thenReturn(Arrays.asList(seller));

        List<Seller> sellers = sellerController.getAllSellers();
        assertEquals(1, sellers.size());
        assertEquals("Alice Cooper", sellers.get(0).getName());
    }

    // Тест получения продавца по ID
    @Test
    void testGetSellerById() {
        when(sellerService.getSellerById(1L)).thenReturn(Optional.of(seller));

        ResponseEntity<Seller> response = sellerController.getSellerById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seller, response.getBody());
    }

    // Тест создания продавца
    @Test
    void testCreateSeller() {
        when(sellerService.createSeller(seller)).thenReturn(seller);

        ResponseEntity<Seller> response = sellerController.createSeller(seller);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(seller, response.getBody());
    }

    // Тест обновления продавца
    @Test
    void testUpdateSeller() {
        Seller updatedSeller = new Seller();
        updatedSeller.setName("Updated Name");

        when(sellerService.updateSeller(1L, updatedSeller)).thenReturn(Optional.of(updatedSeller));

        ResponseEntity<Seller> response = sellerController.updateSeller(1L, updatedSeller);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSeller, response.getBody());
    }

    // Тест удаления продавца
    @Test
    void testDeleteSeller() {
        when(sellerService.deleteSeller(1L)).thenReturn(true);

        ResponseEntity<Void> response = sellerController.deleteSeller(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(sellerService, times(1)).deleteSeller(1L);
    }

     // Тест получения самого продуктивного продавца за указанный период
    @Test
    void testGetTopSeller() {
        when(sellerService.getTopSellerByPeriod(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(seller));

        ResponseEntity<Seller> response = sellerController.getTopSeller(LocalDateTime.now(), LocalDateTime.now());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(seller, response.getBody());
    }

    // Тест получения списка продавцов с суммой транзакций меньше указанного значения
    @Test
    void testGetSellersWithTotalAmountLessThan() {
        when(sellerService.getSellersWithTotalAmountLessThan(any(Double.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(seller));

        List<Seller> sellers = sellerController.getSellersWithTotalAmountLessThan(1000.0, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(1, sellers.size());
        assertEquals(seller, sellers.get(0));
    }

    // Тест получения лучшего периода транзакций для конкретного продавца
    @Test
    void testGetBestTransactionPeriod() {
        when(sellerService.getSellerById(1L)).thenReturn(Optional.of(seller));
        when(sellerService.getBestTransactionPeriod(seller)).thenReturn(Optional.of(bestPeriodResult));

        ResponseEntity<BestPeriodResult> response = sellerController.getBestTransactionPeriod(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bestPeriodResult, response.getBody());
    }
}
