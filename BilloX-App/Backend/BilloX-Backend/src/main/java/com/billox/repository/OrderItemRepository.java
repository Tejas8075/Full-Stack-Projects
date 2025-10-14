package com.billox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billox.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

}
