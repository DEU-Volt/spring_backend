package deu.se.volt.microservices.core.order.repository;

import deu.se.volt.microservices.core.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
