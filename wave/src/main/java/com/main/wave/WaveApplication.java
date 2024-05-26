package com.main.wave;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.main.wave.mapper")
public class WaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaveApplication.class, args);
    }

}
