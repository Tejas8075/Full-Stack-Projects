package com.billox.io;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {

	private Double todaySales;
	
	private Long todayOrderCount;
	
	private List<OrderResponse> recentOrders;
	
}
