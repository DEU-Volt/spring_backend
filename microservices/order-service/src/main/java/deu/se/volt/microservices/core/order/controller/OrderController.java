package deu.se.volt.microservices.core.order.controller;


import deu.se.volt.microservices.core.order.entity.OrderEntity;
import deu.se.volt.microservices.core.order.form.OrderForm;
import deu.se.volt.microservices.core.order.service.OrderService;
import deu.se.volt.microservices.core.order.util.DefaultResponse;
import deu.se.volt.microservices.core.order.util.ResponseMessage;
import deu.se.volt.microservices.core.order.util.StatusCode;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

     @PostMapping("/order")
     public ResponseEntity postOrder(@RequestHeader(value="Authorization") String accessToken, @RequestBody @Valid OrderForm orderForm) {
         // JWT Token to username

         try {
             Map<String, OrderEntity> map = new HashMap<>();
             var orderEntity = orderService.createOrderEntity(modelMapper.map(orderForm, OrderEntity.class));
             map.put("result", orderService.save(accessToken,orderEntity));

             return new ResponseEntity(DefaultResponse.res(
                     StatusCode.OK,
                     ResponseMessage.ORDER_REG_SUCCESS,
                     map), HttpStatus.OK);

         } catch (NotFoundException notFoundException) {
             Map<String, String> map = new HashMap<>();
             map.put("result","false");

             return new ResponseEntity(DefaultResponse.res(
                     StatusCode.NOT_FOUND,
                     ResponseMessage.NOT_FOUND_PRODUCT,
                     map), HttpStatus.OK);

     }
     }

//    @DeleteMapping("/order/{path}")
//    public ResponseEntity getProductByProductName(@PathVariable("orderId") @Valid final int orderId) {
//
//        try {
//            Map<String, Product> map = new HashMap<>();
//            var product = productService.loadProductByProductName(productName);
//            map.put("result", product);
//
//            return new ResponseEntity(DefaultResponse.res(
//                    StatusCode.OK,
//                    ResponseMessage.PRODUCT_SUCCESS,
//                    map), HttpStatus.OK);
//
//        } catch (NoSuchElementException elementException) {
//            Map<String, String> map = new HashMap<>();
//            map.put("result","false");
//
//            return new ResponseEntity(DefaultResponse.res(
//                    StatusCode.NOT_FOUND,
//                    ResponseMessage.NOT_FOUND_PRODUCT,
//                    map), HttpStatus.OK);
//        }
//    }


}
