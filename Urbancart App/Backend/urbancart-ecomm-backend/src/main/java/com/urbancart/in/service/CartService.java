package com.urbancart.in.service;

import com.urbancart.in.exception.ProductException;
import com.urbancart.in.model.Cart;
import com.urbancart.in.model.CartItem;
import com.urbancart.in.model.Product;
import com.urbancart.in.model.User;

public interface CartService {
	
	public CartItem addCartItem(User user,
								Product product,
								String size,
								int quantity) throws ProductException;
	
	public Cart findUserCart(User user);

}
