package com.czdx.parkingorder;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ParkingOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingOrderApplication.class, args);
	}

}
