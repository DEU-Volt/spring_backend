package deu.se.volt.microservices.core.order.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AlreadyOrderException extends RuntimeException {

    private static final String ERR_MSG = "이미 동일한 모델에 대한 주문이 존재합니다.";
}
