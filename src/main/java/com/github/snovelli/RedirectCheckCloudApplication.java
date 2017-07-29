package com.github.snovelli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedirectCheckCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedirectCheckCloudApplication.class, args);
    }

    @Bean
    public static StorageService getStorageService() {
        return new FileSystemStorageService();
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

}
