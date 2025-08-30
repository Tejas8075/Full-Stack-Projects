package com.urbancart.in.repository;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.Cart;


public interface CartRepository extends JpaRepository<Cart, Long> {

	 Cart findByUserId(Long userId);
}
