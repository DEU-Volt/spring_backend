package deu.se.volt.microservices.core.transaction.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Transaction extends BaseEntity{
    // 자동으로 생성되는 주문 내역 ID(PRIMARY KEY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @NotNull
    private Integer transactionPrice;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductGrade productGrade;
}