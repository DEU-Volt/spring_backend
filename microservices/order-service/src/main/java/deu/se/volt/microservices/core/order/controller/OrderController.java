package deu.se.volt.microservices.core.order.controller;

import deu.se.volt.microservices.core.order.entity.OrderEntity;
import deu.se.volt.microservices.core.order.entity.OrderStatusType;
import deu.se.volt.microservices.core.order.entity.OrderType;
import deu.se.volt.microservices.core.order.service.OrderService;
import deu.se.volt.microservices.core.order.util.DefaultResponse;
import deu.se.volt.microservices.core.order.util.ResponseMessage;
import deu.se.volt.microservices.core.order.util.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@Api("주문 컨트롤러 V1")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    /*
        GET / 사용자 ID로 주문 조회 / Return : OrderEntity
    */
    @ApiOperation(value = "사용자 ID로 조회", tags = "주문 관리",
            httpMethod = "GET",
            notes = "사용자 ID를 사용하여 주문을 조회할 때 사용되는 API 입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/order/user")
    public ResponseEntity getOrderByUserName() {
        var userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var orderEntities = orderService.loadOrderByUsername(userName);
        if(orderEntities.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_ORDER,
                    map), HttpStatus.OK);
        }else {
            Map<String, List<OrderEntity>> map = new HashMap<>();
            map.put("result", orderEntities);

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);

        }
    }
    /*
        GET / 모델명으로 주문 조회 / Return : OrderEntity
    */
    @ApiOperation(value = "모델명으로 조회", tags = "주문 관리",
            httpMethod = "GET",
            notes = "모델명을 사용하여 주문을 조회할 때 사용되는 API 입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/order/model/{modelName}")
    public ResponseEntity getOrderByModelName(@PathVariable("modelName")@Valid final String modelName) {
        var orderStatusType = OrderStatusType.CONFIRM;
        var orderSellType = OrderType.SELL;
        var orderBuyType = OrderType.BUY;
        var userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var orderSellEntities = orderService.loadOrderByModelName(modelName, orderStatusType, orderSellType);
        var orderBuyEntities = orderService.loadOrderByModelNameDesc(modelName, orderStatusType, orderBuyType);

        if(orderSellEntities.isEmpty() && orderBuyEntities.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_ORDER,
                    map), HttpStatus.OK);
        } else if (orderSellEntities.isEmpty()) {
            Map<String, List<OrderEntity>> map = new HashMap<>();
            map.put("buy", orderBuyEntities);

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);

        } else if (orderBuyEntities.isEmpty()) {
            Map<String, List<OrderEntity>> map = new HashMap<>();
            map.put("sell", orderSellEntities);

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);

        } else {
            Map<String, List<OrderEntity>> map = new HashMap<>();
            map.put("sell", orderSellEntities);
            map.put("buy", orderBuyEntities);

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);
        }
    }
}