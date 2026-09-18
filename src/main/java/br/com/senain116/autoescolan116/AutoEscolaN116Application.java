package br.com.senain116.autoescolan116;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AutoEscolaN116Application {
    public static void main(String[] args) {
        SpringApplication.run(AutoEscolaN116Application.class, args);
    }
}