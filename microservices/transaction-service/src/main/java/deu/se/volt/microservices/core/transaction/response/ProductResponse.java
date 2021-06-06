package deu.se.volt.microservices.core.transaction.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductResponse {

    @JsonProperty("statusCode")
    private int statusCode;
}
