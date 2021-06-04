package deu.se.volt.microservices.core.product.controller;

import deu.se.volt.authorizationserver.util.StatusCode;
import deu.se.volt.microservices.core.product.entity.Product;
import deu.se.volt.microservices.core.product.service.ProductService;
import deu.se.volt.microservices.core.product.util.DefaultResponse;
import deu.se.volt.microservices.core.product.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Api("상품 컨트롤러 V1")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    /*
        GET / 상품명으로 상품조회 / Return : Product
     */
    @ApiOperation(value = "test", notes = "테스트입니다.")
    @GetMapping("/product/prd/{productName}")
    public ResponseEntity getProductByProductName(@PathVariable("productName") @Valid final String productName) {

        try {
            Map<String, Product> map = new HashMap<>();
            var product = productService.loadProductByProductName(productName);
            map.put("result", product);

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);

        } catch (NoSuchElementException elementException) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_PRODUCT,
                    map), HttpStatus.OK);
        }
    }

    /*
        GET / 모델명으로 상품 조회 / Return : Product
    */
    @GetMapping("/product/model/{modelName}")
    public ResponseEntity getProductByModelName(@PathVariable("modelName") @Valid final String modelName) {

        try {
            Map<String, Product> map = new HashMap<>();
            var product = productService.loadProductByModelName(modelName);
            map.put("result", product);

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);

        } catch (NoSuchElementException elementException) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_PRODUCT,
                    map), HttpStatus.OK);
        }
    }
}
