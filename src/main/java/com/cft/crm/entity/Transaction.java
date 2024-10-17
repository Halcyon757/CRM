package com.cft.crm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент для ID
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false) // Внешний ключ на продавца
    private Seller seller;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_type", nullable = false)
    private String paymentType; // Возможные значения: CASH, CARD, TRANSFER

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    // Конструкторы

    public Transaction() {}

    public Transaction(Seller seller, BigDecimal amount, String paymentType, LocalDateTime transactionDate) {
        this.seller = seller;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
