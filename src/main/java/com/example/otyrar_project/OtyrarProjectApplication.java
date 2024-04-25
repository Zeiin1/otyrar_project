package com.example.otyrar_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class OtyrarProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtyrarProjectApplication.class, args);
    }

}
