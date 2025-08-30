package com.urbancart.in.response;


import com.urbancart.in.dto.OrderHistory;
import com.urbancart.in.model.Cart;
import com.urbancart.in.model.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {
    private String functionName;
    private Cart userCart;
    private OrderHistory orderHistory;
    private Product product;
}
