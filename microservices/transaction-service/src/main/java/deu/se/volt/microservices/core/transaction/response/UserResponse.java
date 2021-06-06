package deu.se.volt.microservices.core.transaction.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {

    @JsonProperty("statusCode")
    private int statusCode;
}
