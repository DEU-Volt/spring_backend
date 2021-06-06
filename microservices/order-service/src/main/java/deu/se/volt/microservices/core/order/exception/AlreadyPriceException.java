package deu.se.volt.microservices.core.order.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AlreadyPriceException extends RuntimeException {

    private static final String ERR_MSG = "신청한 금액보다 유리하거나 동일한 매물이 존재합니다.";
}
