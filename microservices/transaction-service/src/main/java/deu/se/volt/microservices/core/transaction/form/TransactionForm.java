package deu.se.volt.microservices.core.transaction.form;
import deu.se.volt.microservices.core.transaction.entity.ProductGrade;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TransactionForm {

    @NotBlank
    private String seller;

    @NotBlank
    private String buyer;

    @NotBlank
    private String modelName;

    @NotNull
    private Integer transactionPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductGrade productGrade;
}
