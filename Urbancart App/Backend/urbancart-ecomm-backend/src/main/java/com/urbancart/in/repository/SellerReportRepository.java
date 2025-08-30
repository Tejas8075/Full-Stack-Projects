package com.urbancart.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport,Long> {
    SellerReport findBySellerId(Long sellerId);
}
