package com.example.TulipApplication.dto;

import com.example.TulipApplication.entities.Cart;
import com.example.TulipApplication.entities.CartItem;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartResponse {

    private Long cartId;
    private List<CartItemResponse> items;
    private Double total;
    private Integer totalItems;

    public static CartResponse fromCart(Cart cart){
        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());
        response.setItems(cart.getItems().stream()
                .map(CartItemResponse::fromCartItem)
                .collect(Collectors.toList()));
        response.setTotal(cart.getTotal());
        response.setTotalItems(cart.getTotalItems());

        return response;
    }

}
