package com.billox.service;

import com.billox.io.RazorpayOrderResponse;
import com.razorpay.RazorpayException;

public interface RazorpayService {

	RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException;
	
}
