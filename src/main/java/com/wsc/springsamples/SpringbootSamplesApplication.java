package com.wsc.springsamples;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wsc.springsamples.mappers")
public class SpringbootSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSamplesApplication.class, args);
    }

}
