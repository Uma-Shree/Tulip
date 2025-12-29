package com.example.TulipApplication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private String name;

    // One Category can have many Products
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

    //Getters and setters
    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}

    public String getName() { return name;}
    public  void setName(String name) { this.name = name;}

    public List<Product> getProducts() { return products;}
    public void setProducts(List<Product> products) { this.products = products;}
}
