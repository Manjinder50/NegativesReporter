package com.practice.NegativesReporter.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

@Service
public class S3UploadImpl implements S3UploadService {

	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public void uploadExcelFile(File file, String s3Url) {
		
		WritableResource resource = (WritableResource) resourceLoader.getResource(s3Url);

		try (OutputStream outputStream = resource.getOutputStream()) {
			Files.copy(file.toPath(), outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
