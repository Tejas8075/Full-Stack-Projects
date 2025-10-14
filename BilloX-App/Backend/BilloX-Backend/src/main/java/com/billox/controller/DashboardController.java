package com.billox.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billox.io.DashboardResponse;
import com.billox.io.OrderResponse;
import com.billox.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

	private final OrderService oService;

	@GetMapping
	DashboardResponse getDashboardData() {

		LocalDate today = LocalDate.now();

		Double todaySale = oService.sumSalesByDate(today);

		Long todayOrderCount = oService.countByOrderDate(today);

		List<OrderResponse> recentOrders = oService.findRecentOrders();

		return new DashboardResponse(

				todaySale != null ? todaySale : 0.0, 
				todayOrderCount != null ? todayOrderCount : 0,
				recentOrders

		);

	}

}
