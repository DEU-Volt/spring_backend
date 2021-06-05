package deu.se.volt.microservices.core.order.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@Table(indexes = @Index(name = "i_modelName",columnList = "modelName"))
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long orderIdx;

    @Column
    @NotBlank
    @Enumerated(EnumType.STRING)
    OrderType orderType;

    @Column
    @NotBlank
    @Enumerated(EnumType.STRING)
    OrderStatusType orderStatusType = OrderStatusType.CONFIRM;

    @Column
    @NotBlank
    @Enumerated(EnumType.STRING)
    ProductGrade productGrade = ProductGrade.NONE;

    @Column
    @NotBlank
    String username;

    @Column
    @NotBlank
    String modelName;

    @Column
    @NotBlank
    Integer orderPrice;

    @CreatedDate
    @NotBlank
    @Column
    private LocalDateTime expiredAt;

    @CreatedDate
    @NotBlank
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @NotBlank
    private LocalDateTime updatedAt;

}
