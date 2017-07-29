package com.github.snovelli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpSession;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class RedirectCheckCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedirectCheckCloudApplication.class, args);
    }

    @Bean
    public static StorageService getStorageService() {
        return new FileSystemStorageService(Paths.get("."));
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

    @Bean
    public ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(2);
    }

}
