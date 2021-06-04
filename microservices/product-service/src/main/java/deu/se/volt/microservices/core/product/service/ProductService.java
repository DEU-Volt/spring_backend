package deu.se.volt.microservices.core.product.service;

import deu.se.volt.microservices.core.product.entity.Product;
import deu.se.volt.microservices.core.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {

    ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product loadProductByProductName(String productName) throws NoSuchElementException {

        return productRepository.findByProductName(productName).orElseThrow();
    }

    public Product loadProductByModelName(String modelName) {
        return productRepository.findByModelName(modelName).orElseThrow();
    }
    
    public Product loadProductByManufacturer(String manufacturer) {
        return productRepository.findByManufacturer(manufacturer).orElseThrow();
    }
}
