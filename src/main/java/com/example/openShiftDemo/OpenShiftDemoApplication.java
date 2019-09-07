package com.example.openShiftDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OpenShiftDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenShiftDemoApplication.class, args);
	}

	@RequestMapping("/test")
	public String test(){
		return "Test Response";
	}

}
