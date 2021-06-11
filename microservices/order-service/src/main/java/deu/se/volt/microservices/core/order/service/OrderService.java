package deu.se.volt.microservices.core.order.service;

import deu.se.volt.microservices.core.order.entity.OrderEntity;
import deu.se.volt.microservices.core.order.entity.OrderStatusType;
import deu.se.volt.microservices.core.order.entity.OrderType;
import deu.se.volt.microservices.core.order.exception.AlreadyOrderException;
import deu.se.volt.microservices.core.order.exception.AlreadyPriceException;
import deu.se.volt.microservices.core.order.repository.OrderRepository;
import deu.se.volt.microservices.core.order.response.ProductResponse;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public List<OrderEntity> loadOrderByUsername(String userName) {
        return orderRepository.findOrderEntitiesByUsername(userName);
    }

    public List<OrderEntity> loadOrderByModelName(String modelName, OrderStatusType orderStatusType, OrderType orderType) {
        return orderRepository.findOrderEntitiesByModelNameAndOrderStatusTypeAndOrderTypeOrderByOrderPriceAscCreatedAtAsc(modelName, orderStatusType, orderType);
    }


    public List<OrderEntity> loadOrderByModelNameDesc(String modelName, OrderStatusType orderStatusType, OrderType orderType) {
        return orderRepository.findOrderEntitiesByModelNameAndOrderStatusTypeAndOrderTypeOrderByOrderPriceDescCreatedAtAsc(modelName, orderStatusType, orderType);
    }


    /*
        OrderEntity를 저장해주는 함수
     */
    public OrderEntity save(String accessToken, OrderEntity orderEntity) throws NotFoundException, AlreadyOrderException, AlreadyPriceException{
        var priceCheckMap = checkAlreadyPriceExist(orderEntity);
        try {
            // 해당 모델이 상품 서비스에 존재하지 않을경우 Return
            if (!checkExistProduct(accessToken, orderEntity.getModelName())) {
                throw new NotFoundException(orderEntity.getModelName());
            }

            // 해당 모델에 대한 주문이 이미 존재할 경우 Return
            else if (!loadOrderByUsername(orderEntity.getUsername()).isEmpty()) {
                throw new AlreadyOrderException();
            }

            // 해당 주문보다 유리한 주문이 존재할 경우 해당 오더를 결제 서비스로 이관 -> 성공시 오더 삭제
            else if (priceCheckMap.containsKey(false)) {
                // 결제 서비스로 리다이렉트하기
                if (redirectTransaction(accessToken,orderEntity,priceCheckMap.get(false))) {
                    deleteOrderEntityByEntity(priceCheckMap.get(false));
                    log.info("결제시스템으로 주문이 이관되어 삭제되었습니다 : {}",priceCheckMap.get(false).getOrderIdx());
                    throw new AlreadyPriceException();
                }

                // 임시 오더 삭제
                // if (deleteOrderEntity(orderEntity))

            }
        } catch (NullPointerException | RestClientException exception) {
            log.error(exception.toString());
        }

        // 위 조건들을 만족했을 때 Insert.
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    /*
        상품서비스에 상품이 존재하는지 여부를 확인해주는 함수
     */
    private boolean checkExistProduct(String accessToken, String modelName) throws RestClientException, NullPointerException {
        var header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, accessToken);
        var response = new RestTemplate().exchange("http://localhost:18081/product/model/" + modelName
                , HttpMethod.GET, new HttpEntity(header), ProductResponse.class);

        return Objects.requireNonNull(response.getBody()).getStatusCode() == 200;
    }

    /*
         구매 또는 판매 신청을 했을 때 희망하는 가격이 오더 목록에 존재하는 매물들의 가격대보다 높을 경우 예외발생
         즉시 결제로 리다이렉팅
         미구현
     */
    private Map<Boolean, OrderEntity> checkAlreadyPriceExist(OrderEntity orderEntity) {
        HashMap<Boolean, OrderEntity> returnMap = new HashMap<>();
        if (orderEntity.getOrderType() == OrderType.BUY) {
            var sellOrderList = loadOrderByModelName(orderEntity.getModelName(), OrderStatusType.CONFIRM, OrderType.SELL);
            if (!sellOrderList.isEmpty() && sellOrderList.get(0).getOrderPrice() <= orderEntity.getOrderPrice()) {
                returnMap.put(false, sellOrderList.get(0));
                return returnMap;
            }
        } else if (orderEntity.getOrderType() == OrderType.SELL) {
            var buyOrderList = loadOrderByModelNameDesc(orderEntity.getModelName(), OrderStatusType.CONFIRM, OrderType.BUY);
            if (!buyOrderList.isEmpty() && buyOrderList.get(0).getOrderPrice() >= orderEntity.getOrderPrice()) {
                returnMap.put(false, buyOrderList.get(0));
                return returnMap;
            }
        }
        returnMap.put(true, null);
        return returnMap;
    }

    private boolean redirectTransaction(String accessToken, OrderEntity userOrderEntity, OrderEntity otherOrderEntity) {
        var header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, accessToken);
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> map = new HashMap<>();
        if(userOrderEntity.getOrderType() == OrderType.BUY) {
            map.put("seller", otherOrderEntity.getUsername());
            map.put("buyer", userOrderEntity.getUsername());
        }
        else if(userOrderEntity.getOrderType() == OrderType.SELL) {
            map.put("seller", userOrderEntity.getUsername());
            map.put("buyer", otherOrderEntity.getUsername());
        }
        map.put("modelName", userOrderEntity.getModelName());
        map.put("transactionPrice", userOrderEntity.getOrderPrice());
        map.put("productGrade", userOrderEntity.getProductGrade());


        String body = null;
        try {
            body = objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(body != null) {
            var response = new RestTemplate().exchange("http://localhost:18083/transaction"
                    , HttpMethod.POST, new HttpEntity(body, header), ProductResponse.class);
            return Objects.requireNonNull(response.getBody()).getStatusCode() == 200;
        }
            return false;
    }

    /*
        해당 물품의 가격이 상,하한선을 초과할 경우
        추후 Transaction Module에서 API 호출 구현
     */
    private Boolean checkPriceVaildBand() throws NotFoundException {
        return true;

    }

    /*
        orderEntity르 생성해주는 함수. 불완전한 orderEntity -> 실질적인 Entity로 변환
     */
    public OrderEntity createOrderEntity(OrderEntity orderEntity) {
        orderEntity.setUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        orderEntity.setExpiredAt(LocalDateTime.now().plusDays(1));

        // SELL 일 경우 orderEntity -> WAIT로 Status 변경
        // TEST 시에만 SELL 시에도 검수 없이 CONFIRM
        orderEntity.setOrderStatusType(OrderStatusType.CONFIRM);

//        if (orderEntity.getOrderType() == OrderType.SELL) {
//
//            orderEntity.setOrderStatusType(OrderStatusType.WAIT);
//        }
        if (orderEntity.getOrderType() == OrderType.BUY) {
            orderEntity.setOrderStatusType(OrderStatusType.CONFIRM);
        }

        return orderEntity;
    }

    /*
        orderID를 매개변수로 오더를 삭제할 수 있는 함수.
     */
    public boolean deleteOrderEntityById(Long orderEntityId) {
        try {
            var username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 사용자 검증
            if (orderRepository.findById(orderEntityId).orElseThrow().getUsername().equals(username)) {
                log.info("{} 사용자가 {}를 삭제했습니다.", username, orderEntityId);
                orderRepository.deleteById(orderEntityId);
                return true;
            }
            else
                return false;


        } catch (IllegalArgumentException | NoSuchElementException exception) {
            log.error("삭제 에러 : {}", exception.getMessage());
            return false;
        }
    }


    /*
        OrderEntity로 Order을 삭제할 수 있는 함수.
        내부적 또는 관리자만 사용. 해당 함수는 사용자 검사 로직 X
     */
    public boolean deleteOrderEntityByEntity(OrderEntity orderEntity) {
        try {
            var username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            log.info("{} 사용자가 {}를 삭제했습니다.", username, orderEntity.getOrderIdx());
            orderRepository.delete(orderEntity);
            return true;


        } catch (IllegalArgumentException | NoSuchElementException exception) {
            log.error("삭제 에러 : {}", exception.getMessage());
            return false;
        }
    }

}
