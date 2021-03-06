package deu.se.volt.microservices.core.product.controller;

import deu.se.volt.authorizationserver.util.StatusCode;
import deu.se.volt.microservices.core.product.entity.Product;
import deu.se.volt.microservices.core.product.service.ProductService;
import deu.se.volt.microservices.core.product.util.DefaultResponse;
import deu.se.volt.microservices.core.product.util.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@Api("상품 컨트롤러 V1")
@AllArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    /*
    * GET / 검색 / Return : List<Product>
    */
    @ApiOperation(value = "검색으로 조회", tags = "상품 관리",
            httpMethod = "GET",
            notes = "상품명이나 모델명을 사용하여 상품을 조회할 때 사용되는 API입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/product/search/{searchStr}")
    public ResponseEntity getProductsBySearchStr(@PathVariable("searchStr") @Valid final String searchStr) {
        //  정규 표현식을 이용한 검색어 수정
        var fixedStr = searchStr;
        var iPhoneList = Arrays.asList(new String[]{"iphone","iPhone","IPHONE"});
        var iPadList = Arrays.asList(new String[]{"ipad","iPad","IPAD"});
        var galaxyList = Arrays.asList(new String[]{"galaxy","GALAXY", "겔럭시"});
        for (String str: iPhoneList) {
            fixedStr = fixedStr.replace(str, "아이폰");
        }
        for (String str: iPadList) {
            fixedStr = fixedStr.replace(str, "아이패드");
        }
        for (String str: galaxyList) {
            fixedStr = fixedStr.replace(str, "갤럭시");
        }
        fixedStr = fixedStr.toUpperCase();
        fixedStr = fixedStr.replace(" ", "");
        fixedStr = fixedStr.replaceAll("([갤럭시]+)|([ㄱ-ㅎㅏ-ㅣ가-힣][0-9]+)|([\\^\\-][a-zA-Z][0-9]+)","$0 ");
        var products = productService.loadProductsBySearchStr(fixedStr);
        if (products.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result", "false");
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_PRODUCT,
                    map), HttpStatus.OK);
        } else {
            Map<String, List<Product>> map = new HashMap<>();
            map.put("result", products);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);
        }
    }

    /*
        GET / 상품명으로 상품조회 / Return : Product
    */
    @ApiOperation(value = "상품명으로 조회", tags = "상품 관리",
            httpMethod = "GET",
            notes = "상품명을 사용하여 상품을 조회할 때 사용되는 API입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/product/prd/{productName}")
    public ResponseEntity getProductByProductName(@PathVariable("productName") @Valid final String productName) {
        var products = productService.loadProductByProductName(productName);
        if (products.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_PRODUCT,
                    map), HttpStatus.OK);

        } else {
            Map<String, List<Product>> map = new HashMap<>();
            map.put("result", products);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);

        }
    }

    /*
        GET / 모델명으로 상품 조회 / Return : Product
    */
    @ApiOperation(value = "모델명으로 조회", tags = "상품 관리",
            httpMethod = "GET",
            notes = "모델명을 사용하여 상품을 조회할 때 사용되는 API입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
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

    /*
        GET / productId으로 상품 리스트 조회 / Return : List<Product>
     */
    @ApiOperation(value = "productId으로 조회", tags = "상품 관리",
            httpMethod = "GET",
            notes = "productId을 사용하여 상품을 조회할 때 사용되는 API입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/product/products")
    public ResponseEntity getProductsByProductId() {
        var products = productService.loadProductsByProductIdBetween(1L, 5L);
        if (products.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_PRODUCT,
                    map), HttpStatus.OK);
        } else {
            Map<String, List<Product>> map = new HashMap<>();

            map.put("result", products);

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.PRODUCT_SUCCESS,
                    map), HttpStatus.OK);
        }
    }
}
