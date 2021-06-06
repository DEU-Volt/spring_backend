package deu.se.volt.microservices.core.order.repository;

import deu.se.volt.microservices.core.order.entity.OrderEntity;
import deu.se.volt.microservices.core.order.entity.OrderStatusType;
import deu.se.volt.microservices.core.order.entity.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findOrderEntitiesByUsername(String userName);

    List<OrderEntity> findOrderEntitiesByModelNameAndOrderStatusType (String modelName, OrderStatusType orderStatusType);

    List<OrderEntity> findOrderEntitiesByModelNameAndOrderStatusTypeAndOrderTypeOrderByOrderPriceAscCreatedAtAsc(String modelName, OrderStatusType orderStatusType, OrderType orderType);

    List<OrderEntity> findOrderEntitiesByModelNameAndOrderStatusTypeAndOrderTypeOrderByOrderPriceDescCreatedAtAsc(String modelName, OrderStatusType orderStatusType, OrderType orderType);

}
