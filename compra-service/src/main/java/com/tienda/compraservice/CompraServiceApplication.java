package com.tienda.compraservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CompraServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompraServiceApplication.class, args);
    }
}
