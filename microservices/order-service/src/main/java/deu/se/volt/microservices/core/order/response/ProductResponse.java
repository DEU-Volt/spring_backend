package deu.se.volt.microservices.core.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductResponse {

    @JsonProperty("statusCode")
    private int statusCode;
}
