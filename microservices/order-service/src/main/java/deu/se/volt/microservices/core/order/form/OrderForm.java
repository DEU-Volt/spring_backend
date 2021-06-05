package deu.se.volt.microservices.core.order.form;

import deu.se.volt.microservices.core.order.entity.OrderStatusType;
import deu.se.volt.microservices.core.order.entity.OrderType;
import deu.se.volt.microservices.core.order.entity.ProductGrade;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderForm {


    @NotNull
    private OrderType orderType;

    @NotNull
    private ProductGrade productGrade = ProductGrade.NONE;

    @NotBlank
    private String modelName;

    @NotNull
    private Integer orderPrice;

}
