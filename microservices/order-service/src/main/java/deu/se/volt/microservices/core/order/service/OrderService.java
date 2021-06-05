package deu.se.volt.microservices.core.order.service;

import deu.se.volt.microservices.core.order.entity.OrderEntity;
import deu.se.volt.microservices.core.order.entity.OrderStatusType;
import deu.se.volt.microservices.core.order.entity.OrderType;
import deu.se.volt.microservices.core.order.exception.AlreadyPriceException;
import deu.se.volt.microservices.core.order.form.OrderForm;
import deu.se.volt.microservices.core.order.repository.OrderRepository;
import deu.se.volt.microservices.core.order.response.ProductResponse;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderEntity save(String accessToken, OrderEntity orderEntity) throws NotFoundException {

    try {
        if (!checkExistProduct(accessToken,orderEntity.getModelName())) {
            throw new NotFoundException(orderEntity.getModelName());
        }

        // SELL 일 경우 orderEntity -> WAIT로 Status 변경

        // 해당 모델에 대해 Order가 존재하지 않을 경우에만 save

    } catch (NullPointerException | RestClientException | AlreadyPriceException exception) {

    }
        // 위 조건들을 만족했을 때 Insert.
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    // Get ProductbymodelName API CALL -> 상품명 확인 필요.
    private boolean checkExistProduct(String accessToken, String modelName) throws RestClientException, NullPointerException{
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION,accessToken);
        var response = new RestTemplate().exchange("http://localhost:18081/product/model/" + modelName
                , HttpMethod.GET, new HttpEntity(header), ProductResponse.class);

        return Objects.requireNonNull(response.getBody()).getStatusCode() == 200;
    }

    /*
         구매 또는 판매 신청을 했을 때 희망하는 가격이 오더 목록에 존재하는 매물들의 가격대보다 높을 경우 예외발생
         즉시 결제로 리다이렉팅
         미구현
     */
    private boolean checkAlreadyPriceExist() throws AlreadyPriceException{
        return true;
    }

    /*
        해당 물품의 가격이 상,하한선을 초과할 경우
        추후 Transaction Module에서 API 호출 구현
     */
    private Boolean checkPriceVaildBand() throws NotFoundException {
        return true;

    }


    public OrderEntity createOrderEntity(OrderEntity orderEntity) {
        orderEntity.setUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        orderEntity.setExpiredAt(LocalDateTime.now().plusDays(1));

        if(orderEntity.getOrderType() == OrderType.SELL) {
            orderEntity.setOrderStatusType(OrderStatusType.WAIT);
        }
        else if(orderEntity.getOrderType() == OrderType.BUY){
            orderEntity.setOrderStatusType(OrderStatusType.CONFIRM);
        }

        return orderEntity;


    }

}
