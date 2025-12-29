package com.example.TulipApplication.services;

import com.example.TulipApplication.entities.Product;
import com.example.TulipApplication.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public String deleteProduct(Long id){
        productRepository.deleteById(id);
        return "Product removed! ID : " + id;
    }

    public Product updateProduct(Product product){
        return productRepository.save(product);
    }
}
