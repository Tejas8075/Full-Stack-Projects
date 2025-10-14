package com.billox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billox.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	Optional<OrderEntity> findByOrderId(String orderId);
	
	List<OrderEntity> findAllByOrderByCreatedAtDesc();
	
}
