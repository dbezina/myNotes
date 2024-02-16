package com.bezina.myNotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNotesApplication.class, args);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
