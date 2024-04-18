package com.sojoo.StoreSpotter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy
public class StoreSpotterApplication {
	public static void main(String[] args) {
		SpringApplication.run(StoreSpotterApplication.class, args);
	}


}
