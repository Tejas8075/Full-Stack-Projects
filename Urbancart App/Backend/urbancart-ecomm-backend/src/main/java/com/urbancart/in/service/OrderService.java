package com.urbancart.in.service;

import java.util.List;
import java.util.Set;

import com.urbancart.in.domain.OrderStatus;
import com.urbancart.in.exception.OrderException;
import com.urbancart.in.model.Address;
import com.urbancart.in.model.Cart;
import com.urbancart.in.model.Order;
import com.urbancart.in.model.User;

public interface OrderService {
	
	public Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
	
	public Order findOrderById(Long orderId) throws OrderException;
	
	public List<Order> usersOrderHistory(Long userId);
	
	public List<Order>getShopsOrders(Long sellerId);

	public Order updateOrderStatus(Long orderId,
								   OrderStatus orderStatus)
			throws OrderException;
	
	public void deleteOrder(Long orderId) throws OrderException;

	Order cancelOrder(Long orderId,User user) throws OrderException;
	
}
