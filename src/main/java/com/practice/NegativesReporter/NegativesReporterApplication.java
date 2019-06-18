package com.practice.NegativesReporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.practice.NegativesReporter.service.NegativesReporterService;

@SpringBootApplication
public class NegativesReporterApplication implements CommandLineRunner{

	@Autowired
	private NegativesReporterService negativesReporterService;
	
	public static void main(String[] args) {
		SpringApplication.run(NegativesReporterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		negativesReporterService.processNegativeReports();
	}

}
