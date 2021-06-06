package deu.se.volt.microservices.core.transaction.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class Transaction {
    // 자동으로 생성되는 주문 내역 ID(PRIMARY KEY)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;

    @Column
    @NotBlank
    private String seller;

    @Column
    @NotBlank
    private String buyer;

    @Column
    @NotBlank
    private String modelName;

    @Column
    @NotBlank
    private Integer transactionPrice;

    @Column
    @NotBlank
    @Enumerated(EnumType.STRING)
    private ProductGrade productGrade;

}
