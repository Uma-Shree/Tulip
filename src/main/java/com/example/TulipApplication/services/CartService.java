package com.example.TulipApplication.services;

import com.example.TulipApplication.entities.Cart;
import com.example.TulipApplication.entities.CartItem;
import com.example.TulipApplication.entities.Product;
import com.example.TulipApplication.entities.User;
import com.example.TulipApplication.repositories.CartItemRepository;
import com.example.TulipApplication.repositories.CartRepository;
import com.example.TulipApplication.repositories.ProductRepository;
import com.example.TulipApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    //for logged in user
    public Cart getOrCreateCartForUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found!!"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    //for guest
    public Cart getOrCreateCartForGuest(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setSessionId(sessionId);
                    return cartRepository.save(cart);
                });
    }

    //add item to cart
    @Transactional
    public Cart addItemToCart(Cart cart, Long productId, Integer quantity){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        // Check stock availability
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;

            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Insufficient stock");
            }

            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    //update item quantity
    @Transactional
    public Cart updateItemQuantity(Cart cart, Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Verify item belongs to this cart
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this cart");
        }

        // Check stock
        if (item.getProduct().getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return cartRepository.findById(cart.getId()).orElseThrow();
    }

    //remove item from cart
    @Transactional
    public Cart removeItemFromCart(Cart cart, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Verify item belongs to this cart
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this cart");
        }

        cartItemRepository.delete(item);
        cart.getItems().remove(item);

        return cartRepository.findById(cart.getId()).orElseThrow();
    }

    //clear entire cart
    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    //when guest login then merge cart
    @Transactional
    public Cart mergeGuestCartWithUserCart(String sessionId, String username) {
        Optional<Cart> guestCart = cartRepository.findBySessionId(sessionId);

        if (guestCart.isEmpty()) {
            return getOrCreateCartForUser(username);
        }

        Cart userCart = getOrCreateCartForUser(username);

        // Merge items
        for (CartItem guestItem : guestCart.get().getItems()) {
            Optional<CartItem> existingItem = cartItemRepository
                    .findByCartAndProduct(userCart, guestItem.getProduct());

            if (existingItem.isPresent()) {
                // Update quantity
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + guestItem.getQuantity());
                cartItemRepository.save(item);
            } else {
                // Add to user cart
                guestItem.setCart(userCart);
                cartItemRepository.save(guestItem);
                userCart.getItems().add(guestItem);
            }
        }

        // Delete guest cart
        cartRepository.delete(guestCart.get());

        return cartRepository.save(userCart);
    }

}
