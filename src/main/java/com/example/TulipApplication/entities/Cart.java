package com.example.TulipApplication.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "carts")
public class Cart extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For logged-in users
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // For guest users (session-based)
    @Column(name = "session_id")
    private String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    //helper method to cal total
    public Double getTotal() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

    }

    // Helper method to get total items count
    public Integer getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
