package com.billox.io;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

	private String customerName;
	
	private String phoneNumber;
	
	private List<OrderItemRequest> cartItems;
	
	private Double subTotal;
	
	private Double tax;
	
	private Double grandTotal;
	
	private String paymentMethod;
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderItemRequest{
		
		private String itemId;
		
		private String name;
		
		private Double price;
		
		private Integer quantity;
		
	}
	
}
