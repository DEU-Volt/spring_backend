package deu.se.volt.microservices.core.product.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(indexes = @Index(name = "i_product",columnList = "modelName"))
@Data
public class Product {


    // 자동으로 생성되는 상품 ID(PRIMARY KEY)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    // 상품명
    @Column
    @NotBlank
    private String productName;

    // 모델명
    @Column
    @NotBlank
    private String modelName;

    // 제조사
    @Column
    @NotBlank
    private String manufacturer;


    // 출고가
    @Column
    private Integer shippingPrice;

    // 상품에 대한 사진 주소
    @Column
    private String productPicture;

    // 상품에 대한 썸네일 주소
    @Column
    private String productThumbnail;

    @CreatedDate
    @NotBlank
    @Column(updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @NotBlank
    private LocalDateTime updated_at;


}