package com.urbancart.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {

}
