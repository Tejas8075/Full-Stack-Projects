package com.urbancart.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findReviewsByUserId(Long userId);
    List<Review> findReviewsByProductId(Long productId);
}
