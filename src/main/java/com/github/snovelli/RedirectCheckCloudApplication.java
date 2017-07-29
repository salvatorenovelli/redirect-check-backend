package com.github.snovelli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RedirectCheckCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedirectCheckCloudApplication.class, args);
    }


    @RestController
    class MyController {

        @GetMapping("/hello")
        public String sayHello() {
            return "Hello!";
        }
    }
}
