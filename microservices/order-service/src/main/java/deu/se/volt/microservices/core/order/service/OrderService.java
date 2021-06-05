package deu.se.volt.microservices.core.order.service;

import deu.se.volt.microservices.core.order.entity.OrderEntity;
import deu.se.volt.microservices.core.order.entity.OrderStatusType;
import deu.se.volt.microservices.core.order.entity.OrderType;
import deu.se.volt.microservices.core.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

    public List<OrderEntity> loadOrderByUsername(String userName) {
        return orderRepository.findOrderEntitiesByUsername(userName);
    }

    public List<OrderEntity> loadOrderByModelName(String modelName, OrderStatusType orderStatusType, OrderType orderType) {
        return orderRepository.findOrderEntitiesByModelNameAndOrderStatusTypeAndOrderTypeOrderByOrderPriceAscCreatedAtAsc (modelName, orderStatusType, orderType);
    }

    public List<OrderEntity> loadOrderByModelNameDesc(String modelName, OrderStatusType orderStatusType, OrderType orderType) {
        return orderRepository.findOrderEntitiesByModelNameAndOrderStatusTypeAndOrderTypeOrderByOrderPriceDescCreatedAtAsc(modelName, orderStatusType, orderType);
    }
}
