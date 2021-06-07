package deu.se.volt.microservices.core.transaction.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyStatistics extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dailyStatisticsIdx;

    @Column
    @NotBlank
    private String modelName;

    @Column
    @NotNull
    private int lowestPrice;

    @Column
    @NotNull
    private int highestPrice;

    @Column
    @NotNull
    private double avgPrice;

    @Column
    @NotNull
    private int transactionCount;

    @Column
    @NotNull
    private LocalDate localDate;

}
