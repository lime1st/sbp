package com.lime1st.sbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SbtLime1tApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbtLime1tApplication.class, args);
    }

}
