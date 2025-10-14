package com.billox.io;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RazorpayOrderResponse {

	private String id;
	
	private String entity;
	
	private Integer amount;
	
	private String currency;
	
	private String status;
	
	private Date created_at;
	
	private String receipt;
	
}
