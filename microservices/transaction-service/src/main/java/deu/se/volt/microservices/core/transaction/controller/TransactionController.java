package deu.se.volt.microservices.core.transaction.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api("상품 컨트롤러 V1")
@AllArgsConstructor
class TransactionController {

    @GetMapping("/transaction/test")
    public String test() {
        return "test";
    }
}
