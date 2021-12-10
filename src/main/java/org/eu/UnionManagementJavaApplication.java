package org.eu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class UnionManagementJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnionManagementJavaApplication.class, args);
    }

}
