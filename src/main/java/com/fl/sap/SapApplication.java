package com.fl.sap;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("/hello/integration.xml")
public class SapApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext ctx = new SpringApplication(SapApplication.class).run(args);
		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();
	}
}
