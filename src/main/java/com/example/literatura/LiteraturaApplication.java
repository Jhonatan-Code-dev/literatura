package com.example.literatura;

import com.example.literatura.service.GutendexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

    private final GutendexService gutendexService;

    public LiteraturaApplication(GutendexService gutendexService) {
        this.gutendexService = gutendexService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) {
        gutendexService.menu();
    }
}
