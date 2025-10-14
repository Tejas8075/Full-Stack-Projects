package com.billox.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.billox.io.OrderResponse;
import com.billox.io.PaymentRequest;
import com.billox.io.PaymentVerificationRequest;
import com.billox.io.RazorpayOrderResponse;
import com.billox.service.OrderService;
import com.billox.service.RazorpayService;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final RazorpayService rService;

	private final OrderService oService;

//	@PostMapping("/create-order")
//	@ResponseStatus(HttpStatus.CREATED)
//	RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException{
//		
//		return rService.createOrder(request.getAmount(), request.getCurrency());
//		
//	}

	@PostMapping("/create-order")
	@ResponseStatus(HttpStatus.CREATED)
	RazorpayOrderResponse createRazorpayOrder(@RequestBody PaymentRequest request) throws RazorpayException {
		RazorpayOrderResponse razorpayOrder = rService.createOrder(request.getAmount(), request.getCurrency());

		// Save Razorpay Order ID into Order record
		oService.updateRazorpayOrderId(request.getOrderId(), razorpayOrder.getId());

		return razorpayOrder;
	}

	@PostMapping("/verify")
	OrderResponse verifyPayment(@RequestBody PaymentVerificationRequest request) {

		return oService.verifyPayment(request);

	}

}
