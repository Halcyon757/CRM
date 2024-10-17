package com.cft.crm.repository;

import com.cft.crm.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    // Возможность добавлять кастомные методы запроса при необходимости
}
