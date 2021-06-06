package deu.se.volt.microservices.core.product.repository;

import deu.se.volt.microservices.core.product.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByModelName(String modelName);

    List<Product> findByProductName(String productName);

    Optional<Product> findByManufacturer(String manufacturer);

    List<Product> findProductsByProductIdBetween(Long start, Long end);
}
