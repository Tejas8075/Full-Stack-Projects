package com.urbancart.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.Address;


public interface AddressRepository extends JpaRepository<Address, Long> {

}
