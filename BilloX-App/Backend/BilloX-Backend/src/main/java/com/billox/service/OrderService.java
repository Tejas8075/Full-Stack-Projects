package com.billox.service;

import java.util.List;

import com.billox.io.OrderRequest;
import com.billox.io.OrderResponse;
import com.billox.io.PaymentVerificationRequest;

public interface OrderService {

	OrderResponse createOrder(OrderRequest request);
	
	void deleteOrder(String orderId);
	
	List<OrderResponse> getLatestOrders();
	
	OrderResponse verifyPayment(PaymentVerificationRequest request);
	
    void updateRazorpayOrderId(String orderId, String razorpayOrderId);
	
}
