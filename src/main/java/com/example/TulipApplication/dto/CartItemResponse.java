package com.example.TulipApplication.dto;

import com.example.TulipApplication.entities.CartItem;
import lombok.Data;

@Data
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Double price;
    private Integer quantity;
    private Double subtotal;

    public static CartItemResponse fromCartItem(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());


        response.setProductName(item.getProduct().getName());
        response.setProductImage(item.getProduct().getImageUrl());
        response.setPrice(item.getPrice());
        response.setQuantity(item.getQuantity());
        response.setSubtotal(item.getSubtotal());
        return response;
    }

}
