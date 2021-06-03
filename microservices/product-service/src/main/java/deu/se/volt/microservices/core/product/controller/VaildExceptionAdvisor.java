package deu.se.volt.microservices.core.product.controller;

import deu.se.volt.authorizationserver.util.StatusCode;
import deu.se.volt.microservices.core.product.util.DefaultResponse;
import deu.se.volt.microservices.core.product.util.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class VaildExceptionAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity processValidationError(MethodArgumentNotValidException exception) {
        Map<String, String> map = new HashMap<>() ;
        map.put("result","false");
        return new ResponseEntity(DefaultResponse.res(
                StatusCode.BAD_REQUEST,
                ResponseMessage.INPUT_ERROR,
                map), HttpStatus.BAD_REQUEST);
    }

}