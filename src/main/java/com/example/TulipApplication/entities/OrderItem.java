package com.example.TulipApplication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem extends DateAudit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double price; // Price at the time of purchase

    // Link to the Flower
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Link back to the Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore // Stop infinite loops!
    private ShopOrder order;
}
