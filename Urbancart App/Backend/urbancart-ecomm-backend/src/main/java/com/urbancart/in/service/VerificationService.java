package com.urbancart.in.service;

import com.urbancart.in.model.VerificationCode;

public interface VerificationService {

    VerificationCode createVerificationCode(String otp, String email);
}
