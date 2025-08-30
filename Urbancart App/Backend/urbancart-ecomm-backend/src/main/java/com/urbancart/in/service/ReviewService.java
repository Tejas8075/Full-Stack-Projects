package com.urbancart.in.service;



import javax.naming.AuthenticationException;

import com.urbancart.in.exception.ReviewNotFoundException;
import com.urbancart.in.model.Product;
import com.urbancart.in.model.Review;
import com.urbancart.in.model.User;
import com.urbancart.in.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest req,
                        User user,
                        Product product);

    List<Review> getReviewsByProductId(Long productId);

    Review updateReview(Long reviewId,
                        String reviewText,
                        double rating,
                        Long userId) throws ReviewNotFoundException, AuthenticationException;


    void deleteReview(Long reviewId, Long userId) throws ReviewNotFoundException, AuthenticationException;

}
