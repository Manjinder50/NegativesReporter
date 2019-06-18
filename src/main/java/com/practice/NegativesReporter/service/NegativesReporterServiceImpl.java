package com.practice.NegativesReporter.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.practice.NegativesReporter.ApplicationConstants;
import com.practice.NegativesReporter.model.Customer;
import com.practice.NegativesReporter.model.CustomerList;
import com.practice.NegativesReporter.model.Orders;
import com.practice.NegativesReporter.model.PaginationMetaData;
import com.practice.NegativesReporter.model.Supplier;

@Service
public class NegativesReporterServiceImpl implements NegativesReporterService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private S3UploadImpl s3Upload;
	
	@Value("${endpoint.customers}")
	private String customersEndpoint;

	@Value("${endpoint.orders}")
	private String ordersEndpoint;

	@Value("${pagination.limit}")
	private int limit;
	
	@Value("${app.awsServices.bucketName}")
	private String bucketName;

	@Override
	public void processNegativeReports() {

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		// Add the Jackson Message converter
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

		// Note: here we are making this converter to process any kind of response,
		// not only application/*json, which is the default behaviour
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);

		CustomerList customers = restTemplate.getForObject(customersEndpoint, CustomerList.class);

		for (Customer customer : customers.getCustomers()) {
			{
				for (Supplier supplier : customer.getSuppliers()) {

					Long supplierId = supplier.getId();
					Long customerId = customer.getId();

					try {
						PaginationMetaData pageData = restTemplate
								.getForObject(ordersEndpoint + "?limit=0&start=0", Orders.class).getMetadata();

						performpagination(pageData, customerId, supplierId);

						System.out.println(pageData);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		System.out.println(customers);
	}

	private void performpagination(PaginationMetaData pageData, Long customerId, Long supplierId) {

		String template = ApplicationConstants.template;
		
		long totalCount = pageData.getTotal();

		long noOfrecords = totalCount / limit;

		if (totalCount % limit > 0) {

			noOfrecords += 1;
		}

		System.out.println("Total Count =" + totalCount);
		System.out.println("No. of records in each iteration " + noOfrecords);
		int start = 1;
		for (int i = 0; i < noOfrecords; i++) {
						
			Orders orders = restTemplate.getForObject(
					ordersEndpoint + "?customerId={customerId}&supplierId={supplierId}&start={start}&limit={limit}",
					Orders.class, customerId, supplierId,start,limit);

			System.out.println("Values of limit and start "+limit+" ,"+start);
			
			orders.getOrders().forEach(order->{
				
				Long templateId = order.getTemplateId();
				File initialFile = new File(new ClassPathResource("src\\main\\resources\\static\\"+template+templateId+ApplicationConstants.extension).getPath());
				try (InputStream is = new FileInputStream(initialFile)) {
					 try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
		                Context context = new Context();
		                context.putVar("order", orders);
		                 JxlsHelper.getInstance().processTemplate(is, os,context);
		                 File temp = File.createTempFile("tempfile", ".xlsx"); 
		         		 String fileName = ""+customerId+supplierId+order.getOrderId();
		         	    //write it
		             	    BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
		             	    bw.write(os.toString());
		             	    bw.close();
		             		
		                 s3Upload.uploadExcelFile(temp, "s3://"+bucketName+"/"+fileName+".xlsl");
				}
		            } catch (IOException e) {
						
		            	e.printStackTrace();
					}
			});
			
			start+=limit;
		}

	}

}
