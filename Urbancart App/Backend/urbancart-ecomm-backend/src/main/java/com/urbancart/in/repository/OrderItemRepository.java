package com.urbancart.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.OrderItem;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
