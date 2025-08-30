package com.urbancart.in.config;

public class JwtConstant {
    // Use a secure, long random key (>=32 chars for HS256). Stored here for demo — in production, use vault/env
    public static final String SECRET_KEY = "this_is_a_very_long_and_secure_secret_key_at_least_32_chars_long";
    public static final String JWT_HEADER = "Authorization";
}

