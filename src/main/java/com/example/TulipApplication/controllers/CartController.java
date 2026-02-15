package com.example.TulipApplication.controllers;

import com.example.TulipApplication.dto.AddToCartRequest;
import com.example.TulipApplication.dto.CartResponse;
import com.example.TulipApplication.entities.Cart;
import com.example.TulipApplication.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            Authentication authentication,
            HttpSession session){

        Cart cart;
        if (authentication != null && authentication.isAuthenticated()) {
            // Logged-in user
            cart = cartService.getOrCreateCartForUser(authentication.getName());
        } else {
            // Guest user
            String sessionId = session.getId();
            cart = cartService.getOrCreateCartForGuest(sessionId);
        }

        return ResponseEntity.ok(CartResponse.fromCart(cart));
    }


    //add item to cart
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @RequestBody AddToCartRequest request,
            Authentication authentication,
            HttpSession session){
        Cart cart;
        if (authentication != null && authentication.isAuthenticated()) {
            cart = cartService.getOrCreateCartForUser(authentication.getName());
        } else {
            String sessionId = session.getId();
            cart = cartService.getOrCreateCartForGuest(sessionId);
        }

        cart = cartService.addItemToCart(cart, request.getProductId(), request.getQuantity());

        return ResponseEntity.ok(CartResponse.fromCart(cart));
    }

    //update item quantity
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @PathVariable Long itemId,
            @RequestParam Integer quantity,
            Authentication authentication,
            HttpSession session) {

        Cart cart;
        if (authentication != null && authentication.isAuthenticated()) {
            cart = cartService.getOrCreateCartForUser(authentication.getName());
        } else {
            String sessionId = session.getId();
            cart = cartService.getOrCreateCartForGuest(sessionId);
        }

        cart = cartService.updateItemQuantity(cart, itemId, quantity);

        return ResponseEntity.ok(CartResponse.fromCart(cart));
    }

    //remove item from cart
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Long itemId,
            Authentication authentication,
            HttpSession session) {

        Cart cart;
        if (authentication != null && authentication.isAuthenticated()) {
            cart = cartService.getOrCreateCartForUser(authentication.getName());
        } else {
            String sessionId = session.getId();
            cart = cartService.getOrCreateCartForGuest(sessionId);
        }

        cart = cartService.removeItemFromCart(cart, itemId);

        return ResponseEntity.ok(CartResponse.fromCart(cart));
    }


    //clear entire cart
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(
            Authentication authentication,
            HttpSession session) {

        Cart cart;
        if (authentication != null && authentication.isAuthenticated()) {
            cart = cartService.getOrCreateCartForUser(authentication.getName());
        } else {
            String sessionId = session.getId();
            cart = cartService.getOrCreateCartForGuest(sessionId);
        }

        cartService.clearCart(cart);

        return ResponseEntity.ok("Cart cleared successfully");
    }

}
