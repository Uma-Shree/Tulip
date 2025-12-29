package com.example.TulipApplication.controllers;

import com.example.TulipApplication.entities.Product;
import com.example.TulipApplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Tells Spring this class handles web requests
@RequestMapping("/api/products") // All URLs in this file start with /api/products
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET request to list all flowers
    // URL: http://localhost:8080/api/products
    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    // POST request to add a new flower
    // URL: http://localhost:8080/api/products
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
    }
}
