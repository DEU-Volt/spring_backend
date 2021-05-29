package com.example.swaggerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication
public class SwaggerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerServerApplication.class, args);
    }

}
