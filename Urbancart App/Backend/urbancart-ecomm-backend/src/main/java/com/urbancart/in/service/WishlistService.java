package com.urbancart.in.service;


//import java.util.Optional;

import com.urbancart.in.exception.WishlistNotFoundException;
import com.urbancart.in.model.Product;
import com.urbancart.in.model.User;
import com.urbancart.in.model.Wishlist;

public interface WishlistService {

    Wishlist createWishlist(User user);

    Wishlist getWishlistByUserId(User user);

    Wishlist addProductToWishlist(User user, Product product) throws WishlistNotFoundException;

}

