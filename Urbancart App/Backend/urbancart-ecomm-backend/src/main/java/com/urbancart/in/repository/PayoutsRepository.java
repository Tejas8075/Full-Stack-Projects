package com.urbancart.in.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.domain.PayoutsStatus;
import com.urbancart.in.model.Payouts;

import java.util.List;

public interface PayoutsRepository extends JpaRepository<Payouts,Long> {

    List<Payouts> findPayoutsBySellerId(Long sellerId);
    List<Payouts> findAllByStatus(PayoutsStatus status);
}
