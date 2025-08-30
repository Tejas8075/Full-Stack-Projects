package com.urbancart.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySellerId(Long sellerId);
}
