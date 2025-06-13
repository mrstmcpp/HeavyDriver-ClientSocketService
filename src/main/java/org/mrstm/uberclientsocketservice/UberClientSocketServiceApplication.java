package org.mrstm.uberclientsocketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("org.mrstm.uberentityservice.models")
public class UberClientSocketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberClientSocketServiceApplication.class, args);
    }

}
