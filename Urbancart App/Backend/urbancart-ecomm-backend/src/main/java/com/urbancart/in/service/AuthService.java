package com.urbancart.in.service;

import com.urbancart.in.exception.SellerException;
import com.urbancart.in.exception.UserException;
import com.urbancart.in.request.LoginRequest;
import com.urbancart.in.request.SignupRequest;
import com.urbancart.in.response.AuthResponse;

import jakarta.mail.MessagingException;

public interface AuthService {

    void sentLoginOtp(String email) throws UserException, MessagingException;
    String createUser(SignupRequest req) throws SellerException;
    AuthResponse signin(LoginRequest req) throws SellerException;

}
