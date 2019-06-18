package com.practice.NegativesReporter.service;

import java.io.ByteArrayOutputStream;
import java.io.File;


public interface S3UploadService {

	    void uploadExcelFile(File file, String s3Url);
}
