package com.urbancart.in.service;

import com.urbancart.in.exception.CartItemException;
import com.urbancart.in.exception.UserException;
import com.urbancart.in.model.CartItem;



public interface CartItemService {
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
	
}
