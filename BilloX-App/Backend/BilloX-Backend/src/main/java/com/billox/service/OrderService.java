package com.billox.service;

import java.util.List;

import com.billox.io.OrderRequest;
import com.billox.io.OrderResponse;

public interface OrderService {

	OrderResponse createOrder(OrderRequest request);
	
	void deleteOrder(String orderId);
	
	List<OrderResponse> getLatestOrders();
	
}
