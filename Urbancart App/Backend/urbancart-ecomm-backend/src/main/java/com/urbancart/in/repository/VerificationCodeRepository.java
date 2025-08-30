package com.urbancart.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbancart.in.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
    VerificationCode findByEmail(String email);
    VerificationCode findByOtp(String otp);
}
