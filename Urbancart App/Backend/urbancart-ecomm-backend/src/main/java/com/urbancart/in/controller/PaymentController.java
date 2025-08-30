package com.urbancart.in.controller;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.urbancart.in.domain.PaymentMethod;
import com.urbancart.in.model.Cart;
import com.urbancart.in.model.Order;
import com.urbancart.in.model.PaymentOrder;
import com.urbancart.in.model.Seller;
import com.urbancart.in.model.SellerReport;
import com.urbancart.in.model.User;
import com.urbancart.in.repository.CartItemRepository;
import com.urbancart.in.repository.CartRepository;
import com.urbancart.in.response.ApiResponse;
import com.urbancart.in.response.PaymentLinkResponse;
import com.urbancart.in.service.PaymentService;
import com.urbancart.in.service.SellerReportService;
import com.urbancart.in.service.SellerService;
import com.urbancart.in.service.TransactionService;
import com.urbancart.in.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final UserService userService;
	private final PaymentService paymentService;
	private final TransactionService transactionService;
	private final SellerReportService sellerReportService;
	private final SellerService sellerService;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	@PostMapping("/api/payment/{paymentMethod}/order/{orderId}")
	public ResponseEntity<PaymentLinkResponse> paymentHandler(@PathVariable PaymentMethod paymentMethod,
			@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);

		PaymentLinkResponse paymentResponse;

		PaymentOrder order = paymentService.getPaymentOrderById(orderId);

//        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
//            paymentResponse=paymentService.createRazorpayPaymentLink(user,
//                    order.getAmount(),
//                    order.getId());
//        }
//        else{
//            paymentResponse=paymentService.createStripePaymentLink(user,
//                    order.getAmount(),
//                    order.getId());
//        }

		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@GetMapping("/api/payment/{paymentId}")
	public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId,
			@RequestParam String paymentLinkId, @RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);

		PaymentLinkResponse paymentResponse;

		PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

		boolean paymentSuccess = paymentService.ProceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);
		if (paymentSuccess) {
			for (Order order : paymentOrder.getOrders()) {
				transactionService.createTransaction(order);
				Seller seller = sellerService.getSellerById(order.getSellerId());
				SellerReport report = sellerReportService.getSellerReport(seller);
				report.setTotalOrders(report.getTotalOrders() + 1);
				report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
				report.setTotalSales(report.getTotalSales() + order.getOrderItems().size());
				sellerReportService.updateSellerReport(report);
			}
			Cart cart = cartRepository.findByUserId(user.getId());
			cart.setCouponPrice(0);
			cart.setCouponCode(null);
//        Set<CartItem> items=cart.getCartItems();
//        cartItemRepository.deleteAll(items);
//        cart.setCartItems(new HashSet<>());
			cartRepository.save(cart);

		}

		ApiResponse res = new ApiResponse();
		res.setMessage("Payment successful");
		res.setStatus(true);

		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}
}
