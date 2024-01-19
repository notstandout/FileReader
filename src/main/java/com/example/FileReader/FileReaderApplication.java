package com.example.FileReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class FileReaderApplication {

	public static void main(String[] args) {

		File logsDirectory = new File("logs");
		if (!logsDirectory.exists()) {
			if (logsDirectory.mkdir()) {
				System.out.println("Директория logs создана");
			} else {
				System.err.println("Не удалось создать директорию logs");
			}
		}



		SpringApplication.run(FileReaderApplication.class, args);
	}

}
