package com.example.TulipApplication.repositories;

import com.example.TulipApplication.entities.Cart;
import com.example.TulipApplication.entities.CartItem;
import com.example.TulipApplication.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

    // Find a cart item by cart and product
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
