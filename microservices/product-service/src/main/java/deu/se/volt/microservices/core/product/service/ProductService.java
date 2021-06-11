package deu.se.volt.microservices.core.product.service;

import deu.se.volt.microservices.core.product.entity.Product;
import deu.se.volt.microservices.core.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> loadProductByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

    public Product loadProductByModelName(String modelName) {
        return productRepository.findByModelName(modelName).orElseThrow();
    }
    
    public Product loadProductByManufacturer(String manufacturer) {
        return productRepository.findByManufacturer(manufacturer).orElseThrow();
    }

    public List<Product> loadProductsByProductIdBetween(Long start, Long end) {
        return productRepository.findProductsByProductIdBetween(start, end);
    }

    public List<Product> loadProductsBySearchStr(String searchStr) {
        return productRepository.findProductsByModelNameContainingOrProductNameContaining(searchStr, searchStr);
    }
}
