package deu.se.volt.microservices.core.product.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api
public class ProductController {
    @ApiOperation(value = "test", notes = "테스트입니다.")
    @GetMapping("/product/test")
    public String testFunc() {
        return "test called";
    }
}
