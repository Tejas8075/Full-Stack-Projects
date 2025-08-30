package com.urbancart.in.repository;

//import com.zosh.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	Coupon findByCode(String couponCode);
}
