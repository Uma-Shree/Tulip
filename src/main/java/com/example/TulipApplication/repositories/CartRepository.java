package com.example.TulipApplication.repositories;

import com.example.TulipApplication.entities.Cart;
import com.example.TulipApplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    // Find cart by user (for logged-in users)
    Optional<Cart> findByUser(User user);

    // Find cart by session ID (for guest users)
    Optional<Cart> findBySessionId(String sessionId);


}
