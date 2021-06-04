package deu.se.volt.microservices.core.product.repository;

import deu.se.volt.microservices.core.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByModelName(String modelName);

    Optional<Product> findByProductName(String productName);

    Optional<Product> findByManufacturer(String manufacturer);
}
