package com.example.TulipApplication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product extends DateAudit{
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

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private User supplier;

    public Category getCategory() { return category;}
    public void setCategory(Category category){
        this.category = category;
    }

}

