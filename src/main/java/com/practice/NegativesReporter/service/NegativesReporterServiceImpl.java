package com.practice.NegativesReporter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.practice.NegativesReporter.model.Customer;
import com.practice.NegativesReporter.model.CustomerList;
import com.practice.NegativesReporter.model.Orders;
import com.practice.NegativesReporter.model.PaginationMetaData;
import com.practice.NegativesReporter.model.Supplier;

@Service
public class NegativesReporterServiceImpl implements NegativesReporterService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("customersEndpointKey")
	private String customersEndpoint;
	
	@Value("ordersEndpointKey")
	private String ordersEndpoint;
	

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

					PaginationMetaData pageData = restTemplate.getForObject(ordersEndpoint+"?limit=0&start=0",
							Orders.class).getMetadata();
					
					performpagination(pageData, customerId, supplierId);
					
					System.out.println(pageData);
					
					
					
					
				}
			}

		}
		System.out.println(customers);
	}
	
	private void performpagination(PaginationMetaData pageData,Long customerId,Long supplierId) {
		
		long totalCount = pageData.getTotal();
		long startInit = pageData.getStart();
		long startLimit = pageData.getLimit();
		//for (pageData count) {
		Orders orders = restTemplate.getForObject(ordersEndpoint+"?customerId={customerId}&supplierId={supplierId}&limit=1000&start=0", Orders.class,customerId,supplierId);
		//}
		System.out.println(orders);
	}

}
