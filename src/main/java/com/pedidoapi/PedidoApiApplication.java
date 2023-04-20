package com.pedidoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PedidoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidoApiApplication.class, args);
	}

}
