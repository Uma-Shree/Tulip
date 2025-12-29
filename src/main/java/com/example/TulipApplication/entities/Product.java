package com.example.TulipApplication.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer stock;


    private String imageUrl;

    private String sunlight; // e.g., "Full Sun", "Indoor"

    private String watering; // e.g., "Weekly"

    private boolean isActive = true; // Default to true


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Category getCategory() { return category;}
    public void setCategory(Category category){
        this.category = category;
    }

}

