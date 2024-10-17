package com.cft.crm.service;

import com.cft.crm.entity.Seller;
import com.cft.crm.entity.Transaction;
import com.cft.crm.repository.SellerRepository;
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

class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private SellerService sellerService;

    private Seller seller;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Тестовый продавец
        seller = new Seller();
        seller.setId(1L);
        seller.setName("Alice Cooper");

        // Тестовая транзакция
        transaction = new Transaction(seller, new BigDecimal("100.0"), "CASH", LocalDateTime.of(2024, 1, 1, 10, 0));
    }

    // Тест получения всех продавцов
    @Test
    void testGetAllSellers() {
        when(sellerRepository.findAll()).thenReturn(Arrays.asList(seller));

        List<Seller> sellers = sellerService.getAllSellers();
        assertEquals(1, sellers.size());
        assertEquals(seller, sellers.get(0));
    }

    // Тест получения продавца по ID
    @Test
    void testGetSellerById() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        Optional<Seller> result = sellerService.getSellerById(1L);
        assertTrue(result.isPresent());
        assertEquals(seller, result.get());
    }

    // Тест создания нового продавца
    @Test
    void testCreateSeller() {
        when(sellerRepository.save(seller)).thenReturn(seller);

        Seller createdSeller = sellerService.createSeller(seller);
        assertEquals(seller, createdSeller);
    }

    // Тест обновления информации о продавце
    @Test
    void testUpdateSeller() {
        Seller updatedSeller = new Seller();
        updatedSeller.setName("Updated Name");
        updatedSeller.setContactInfo("updated_contact_info");

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(sellerRepository.save(seller)).thenReturn(updatedSeller);

        Optional<Seller> result = sellerService.updateSeller(1L, updatedSeller);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals("updated_contact_info", result.get().getContactInfo());
    }

    // Тест удаления продавца
    @Test
    void testDeleteSeller() {
        when(sellerRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = sellerService.deleteSeller(1L);
        assertTrue(isDeleted);
        verify(sellerRepository, times(1)).deleteById(1L);
    }

    // Тест получения самого продуктивного продавца за указанный период
    @Test
    void testGetTopSellerByPeriod() {
        when(sellerRepository.findAll()).thenReturn(Arrays.asList(seller));
        when(transactionRepository.findTotalAmountBySellerAndPeriod(eq(seller), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(1000.0);

        Optional<Seller> result = sellerService.getTopSellerByPeriod(LocalDateTime.now(), LocalDateTime.now());
        assertTrue(result.isPresent());
        assertEquals(seller, result.get());
    }

    // Тест получения продавцов с суммой транзакций меньше указанного значения за период
    @Test
    void testGetSellersWithTotalAmountLessThan() {
        when(transactionRepository.findSellersWithTotalAmountLessThan(any(Double.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(seller));

        List<Seller> sellers = sellerService.getSellersWithTotalAmountLessThan(1000.0, LocalDateTime.now(), LocalDateTime.now());
        assertEquals(1, sellers.size());
        assertEquals(seller, sellers.get(0));
    }

    // Тест нахождения наилучшего периода транзакций для продавца
    @Test
    void testGetBestTransactionPeriod() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(seller, new BigDecimal("100.0"), "CASH", LocalDateTime.of(2024, 1, 1, 10, 0)),
                new Transaction(seller, new BigDecimal("150.0"), "CARD", LocalDateTime.of(2024, 1, 2, 12, 0)),
                new Transaction(seller, new BigDecimal("200.0"), "TRANSFER", LocalDateTime.of(2024, 1, 3, 14, 0))
        );

        when(transactionRepository.findBySellerOrderByTransactionDateAsc(seller)).thenReturn(transactions);

        Optional<SellerService.BestPeriodResult> result = sellerService.getBestTransactionPeriod(seller);
        assertTrue(result.isPresent());
        assertEquals(3, result.get().getTransactionCount());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), result.get().getStartDate());
        assertEquals(LocalDateTime.of(2024, 1, 3, 14, 0), result.get().getEndDate());
    }
}
