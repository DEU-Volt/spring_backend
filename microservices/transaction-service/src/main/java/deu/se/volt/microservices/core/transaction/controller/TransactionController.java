package deu.se.volt.microservices.core.transaction.controller;

import deu.se.volt.microservices.core.transaction.entity.Transaction;
import deu.se.volt.microservices.core.transaction.form.TransactionForm;
import deu.se.volt.microservices.core.transaction.service.TransactionService;
import deu.se.volt.microservices.core.transaction.util.DefaultResponse;
import deu.se.volt.microservices.core.transaction.util.ResponseMessage;
import deu.se.volt.microservices.core.transaction.util.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Api("상품 컨트롤러 V1")
@AllArgsConstructor
class TransactionController {
    private final TransactionService transactionService;
    /*
    /POST TRANSACTON API
    */
    @ApiOperation(value = "내역 등록", tags = "내역 관리",
            httpMethod = "POST",
            notes = "내역을 생성하는 API"
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping("/transaction")
    public ResponseEntity postTransaction(@RequestHeader(value="Authorization") String accessToken, @RequestBody @Valid Transaction transaction) {
        // JWT Token to username
        try {
            Map<String, Transaction> map = new HashMap<>();
//            var transaction = transactionService.createEntity(modelMapper.map(orderForm, OrderEntity.class));
            map.put("result", transactionService.save(accessToken, transaction));

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.TRANSACTION_REG_SUCCESS,
                    map), HttpStatus.OK);

        } catch (NotFoundException notFoundException) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.INPUT_ERROR,
                    map), HttpStatus.OK);

        }
    }

    /*
    GET / 모델명으로 내역 조회 / Return : List<Transaction>
    */
    @ApiOperation(value = "모델명으로 조회", tags = "내역 관리",
            httpMethod = "GET",
            notes = "모델명을 사용하여 내역을 조회할 때 사용되는 API 입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/transaction/model/{modelName}")
    public ResponseEntity getTransactionsByModelName(@PathVariable("modelName")@Valid final String modelName) {
        var transactions = transactionService.loadTransactionsByModelName(modelName);

        if(transactions.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_TRANSACTION,
                    map), HttpStatus.OK);
        } else {
            Map<String, List<Transaction>> map = new HashMap<>();
            map.put("result", transactions);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.TRANSACTION_SUCCESS,
                    map), HttpStatus.OK);
        }
    }

    /*
    GET / 사용자 ID로 내역 조회 / Return : List<Transaction>
    */
    @ApiOperation(value = "사용자 ID로 조회", tags = "내역 관리",
            httpMethod = "GET",
            notes = "사용자 ID를 사용하여 내역을 조회할 때 사용되는 API 입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/transaction/user")
    public ResponseEntity getTransactoinsByUserName() {
        var userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var transactions = transactionService.loadTransactionsByUsername(userName);
        var sellTransactions = transactions.stream().filter(a -> a.getSeller().equals(userName)).collect(Collectors.toList());
        var buyTransactions =  transactions.stream().filter(a -> a.getBuyer().equals(userName)).collect(Collectors.toList());

        if(transactions.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result","false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_TRANSACTION,
                    map), HttpStatus.OK);

        } else if(sellTransactions.isEmpty()) {
            Map<String, List<Transaction>> map = new HashMap<>();
            map.put("buy", buyTransactions);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.TRANSACTION_SUCCESS,
                    map), HttpStatus.OK);

        } else if(buyTransactions.isEmpty()) {
            Map<String, List<Transaction>> map = new HashMap<>();
            map.put("sell", sellTransactions);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.TRANSACTION_SUCCESS,
                    map), HttpStatus.OK);
        } else {
            Map<String, List<Transaction>> map = new HashMap<>();
            map.put("sell", sellTransactions);
            map.put("buy", buyTransactions);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.TRANSACTION_SUCCESS,
                    map), HttpStatus.OK);
        }
    }
}
