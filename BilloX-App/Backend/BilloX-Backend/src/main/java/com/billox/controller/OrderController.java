package com.billox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.billox.io.OrderRequest;
import com.billox.io.OrderResponse;
import com.billox.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService oService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	OrderResponse createdOrder(@RequestBody OrderRequest request) {
		
		return oService.createOrder(request);
		
	}
	
	@DeleteMapping("/{orderId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteOrder(@PathVariable String orderId) {
		
		oService.deleteOrder(orderId);
		
	}
	
	@GetMapping("/latest")
	List<OrderResponse> getLatestOrders(){
		
		return oService.getLatestOrders();
		
	}
	
}
