package com.urbancart.in.ai.services;

import com.urbancart.in.exception.ProductException;
import com.urbancart.in.response.ApiResponse;

public interface AiChatBotService {

    ApiResponse aiChatBot(String prompt,Long productId,Long userId) throws ProductException;
}
