package com.example.TechnicalAnalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TechnicalAnalysisApplication {
	public static String SonarQube = "";
	public static void main(String[] args) {
		SpringApplication.run(TechnicalAnalysisApplication.class, args);
	}

	public static boolean isWindows() {
		return System.getProperty("filename.separator").equals("\\");
	}
}
