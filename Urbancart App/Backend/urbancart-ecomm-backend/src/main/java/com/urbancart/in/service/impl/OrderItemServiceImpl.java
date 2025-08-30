package com.urbancart.in.service.impl;

import org.springframework.stereotype.Service;

import com.urbancart.in.exception.OrderException;
import com.urbancart.in.model.OrderItem;
import com.urbancart.in.repository.OrderItemRepository;
import com.urbancart.in.service.OrderItemService;

import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	private final OrderItemRepository orderItemRepository;

	public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
	}

	@Override
	public OrderItem getOrderItemById(Long id) throws Exception {

		System.out.println("------- " + id);
		Optional<OrderItem> orderItem = orderItemRepository.findById(id);
		if (orderItem.isPresent()) {
			return orderItem.get();
		}
		throw new OrderException("Order item not found");
	}
}
