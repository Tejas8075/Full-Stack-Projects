package com.billox.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.billox.entity.OrderEntity;
import com.billox.entity.OrderItemEntity;
import com.billox.io.OrderRequest;
import com.billox.io.OrderResponse;
import com.billox.io.PaymentDetails;
import com.billox.io.PaymentMethod;
import com.billox.repository.OrderRepository;
import com.billox.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository oRepo;

	@Override
	public OrderResponse createOrder(OrderRequest request) {

		OrderEntity newOrder = convertToOrderEntity(request);

		PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails
				.setStatus(newOrder.getPaymentMethod() == PaymentMethod.CASH ? PaymentDetails.PaymentStatus.COMPLETED
						: PaymentDetails.PaymentStatus.PENDING);

		newOrder.setPaymentDetails(paymentDetails);

		List<OrderItemEntity> orderItems = request.getCartItems().stream().map(this::convertToOrderItemEntity)
				.collect(Collectors.toList());

		newOrder.setItems(orderItems);

		newOrder = oRepo.save(newOrder);

		return convertToResponse(newOrder);

	}

	@Override
	public void deleteOrder(String orderId) {
		
		OrderEntity existingOrder = oRepo.findByOrderId(orderId)
			 .orElseThrow(() -> new RuntimeException("Order No Found"));

		oRepo.delete(existingOrder);
		
	}

	@Override
	public List<OrderResponse> getLatestOrders() {
		
		return oRepo.findAllByOrderByCreatedAtDesc()
			 .stream()
			 .map(this :: convertToResponse)
			 .collect(Collectors.toList());
		
	}

	private OrderResponse convertToResponse(OrderEntity newOrder) {

		return OrderResponse.builder().orderId(newOrder.getOrderId()).customerName(newOrder.getCustomerName())
				.phoneNumber(newOrder.getPhoneNumber()).subTotal(newOrder.getSubTotal()).tax(newOrder.getTax())
				.grandTotal(newOrder.getGrandTotal()).paymentMethod(newOrder.getPaymentMethod())
				.items(newOrder.getItems().stream().map(this::convertToItemResponse).collect(Collectors.toList()))
				.paymentDetails(newOrder.getPaymentDetails()).createdAt(newOrder.getCreatedAt()).build();

	}

	private OrderEntity convertToOrderEntity(OrderRequest request) {

		return OrderEntity.builder().customerName(request.getCustomerName()).phoneNumber(request.getPhoneNumber())
				.subTotal(request.getSubTotal()).tax(request.getTax()).grandTotal(request.getGrandTotal())
				.paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod())).build();

	}

	private OrderItemEntity convertToOrderItemEntity(OrderRequest.OrderItemRequest orderItemRequest) {

		return OrderItemEntity.builder().itemId(orderItemRequest.getItemId()).name(orderItemRequest.getName())
				.price(orderItemRequest.getPrice()).quantity(orderItemRequest.getQuantity()).build();

	}

	private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity) {

		return OrderResponse.OrderItemResponse.builder().itemId(orderItemEntity.getItemId())
				.name(orderItemEntity.getName()).price(orderItemEntity.getPrice())
				.quantity(orderItemEntity.getQuantity()).build();

	}

}
