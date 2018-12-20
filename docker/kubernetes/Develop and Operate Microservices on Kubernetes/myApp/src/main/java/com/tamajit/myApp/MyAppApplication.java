package com.tamajit.myApp;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyAppApplication.class, args);
	}
	@RequestMapping("/info")
	public String info() throws UnknownHostException{
		return "Running on "+ InetAddress.getLocalHost().getHostAddress();
	}
	
	@RequestMapping("/greet")
	public String greet() throws UnknownHostException{
		return "Hello Java!!";
	}
}
