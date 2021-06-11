package deu.se.volt.microservices.core.order.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "orderIdx", callSuper = true)
@Table(indexes = @Index(name = "i_modelName",columnList = "modelName"))
public class OrderEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderIdx;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatusType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductGrade productGrade;

    @Column
    private String username;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(nullable = false)
    private LocalDateTime expiredAt;
}